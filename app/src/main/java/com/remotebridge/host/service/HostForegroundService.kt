package com.remotebridge.host.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.remotebridge.core.model.BridgeRequest
import com.remotebridge.core.model.BridgeResponse
import com.remotebridge.core.model.CallLogItem
import com.remotebridge.core.model.Commands
import com.remotebridge.core.model.ContactItem
import com.remotebridge.core.model.DeviceInfo
import com.remotebridge.core.model.FileItem
import com.remotebridge.core.model.SmsMessage
import com.remotebridge.core.signaling.Signal
import com.remotebridge.core.signaling.SignalingClient
import com.remotebridge.core.signaling.SignalingState
import com.remotebridge.core.webrtc.PeerConnectionManager
import com.remotebridge.core.webrtc.PeerState
import com.remotebridge.core.webrtc.FrameProtocol
import com.remotebridge.host.provider.HostDataProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.builtins.ListSerializer
import java.util.concurrent.atomic.AtomicInteger

/**
 * خدمة الجهاز المضيف (الجهاز القديم).
 *
 * المسؤوليات:
 * 1. تبقى تعمل في الخلفية كخدمة أمامية (لن يقتلها أندرويد بسهولة).
 * 2. تتصل بخادم الإشارات وتسجّل الجهاز كـ "مضيف".
 * 3. تستقبل طلبات اتصال WebRTC من جهاز التحكم.
 * 4. تعالج الطلبات (استعراض ملفات، رسائل، …) وتردّ بالبيانات.
 */
class HostForegroundService : LifecycleService() {

    companion object {
        private const val TAG = "HostService"

        const val EXTRA_SERVER_URL = "server_url"
        const val EXTRA_DEVICE_NAME = "device_name"

        const val ACTION_START = "com.remotebridge.host.START"
        const val ACTION_STOP = "com.remotebridge.host.STOP"

        /**
         * حالة الخدمة التي يمكن للواجهة مراقبتها.
         */
        val serviceState = MutableStateFlow(HostServiceState())
    }

    data class HostServiceState(
        val running: Boolean = false,
        val signalingState: SignalingState = SignalingState.Disconnected,
        val peerState: PeerState = PeerState.Idle,
        val controllerConnected: Boolean = false
    )

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val json = Json { ignoreUnknownKeys = true }
    private val requestIdCounter = AtomicInteger(0)

    private var signaling: SignalingClient? = null
    private var peerManager: PeerConnectionManager? = null
    private var dataProvider: HostDataProvider? = null
    private var requestJob: Job? = null

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopHost()
                return START_NOT_STICKY
            }

            else -> {
                val serverUrl = intent?.getStringExtra(EXTRA_SERVER_URL) ?: ""
                val deviceName = intent?.getStringExtra(EXTRA_DEVICE_NAME) ?: "جهاز مضيف"
                startHost(serverUrl, deviceName)
            }
        }
        return START_STICKY
    }

    /**
     * يبدأ الخدمة: إشعار أمامي + اتصال بخادم الإشارات.
     */
    private fun startHost(serverUrl: String, deviceName: String) {
        try {
            NotificationHelper.createChannel(this)
            startForeground(
                NotificationHelper.NOTIFICATION_ID,
                NotificationHelper.buildHostNotification(this)
            )
        } catch (t: Throwable) {
            Log.e(TAG, "فشل بدء الخدمة الأمامية", t)
        }

        dataProvider = HostDataProvider(applicationContext)
        peerManager = PeerConnectionManager(applicationContext, scope).also { manager ->
            manager.start()
        }

        serviceState.value = serviceState.value.copy(running = true)

        scope.launch {
            val deviceId = DeviceIdentifier.getOrGenerate(this@HostForegroundService)
            signaling = SignalingClient(
                url = serverUrl,
                deviceId = deviceId,
                role = "host",
                deviceName = deviceName,
                scope = scope,
                onMessage = { handleSignal(it) },
                onState = { signalingState ->
                    serviceState.value = serviceState.value.copy(signalingState = signalingState)
                }
            ).also { it.connect() }

            collectPeerEvents()
        }
    }

    /**
     * يعالج رسالة إشارات واردة من خادم الإشارات.
     */
    private fun handleSignal(signal: Signal) {
        when (signal) {
            is Signal.Offer -> {
                // جهاز التحكم يطلب اتصالاً → نقبله ونرسل إجابة
                peerManager?.acceptOffer(
                    sdp = signal.sdp,
                    sdpType = signal.sdpType,
                    onSuccess = { answerSdp ->
                        signaling?.send(
                            Signal.Answer(
                                from = signal.to,
                                to = signal.from,
                                sdp = answerSdp,
                                sdpType = "ANSWER"
                            )
                        )
                    },
                    onFailure = { error ->
                        signaling?.send(
                            Signal.Reject(signal.to, signal.from, error)
                        )
                    }
                )
            }

            is Signal.Ice -> {
                peerManager?.addIceCandidate(signal.sdpMid, signal.sdpMLineIndex, signal.sdp)
            }

            is Signal.Reject -> {
                Log.w(TAG, "رُفض الاتصال: ${signal.reason}")
                serviceState.value = serviceState.value.copy(
                    peerState = PeerState.Failed
                )
            }

            is Signal.Pong -> { /* استجابة نبض القلب */ }
            is Signal.Registered -> {
                Log.d(TAG, "تم تسجيل المضيف: ${signal.ok}")
            }

            else -> {}
        }
    }

    /**
     * يجمع أحداث قناة WebRTC (مرشّحات ICE والطلبات الواردة).
     */
    private fun collectPeerEvents() {
        val manager = peerManager ?: return
        requestJob?.cancel()
        requestJob = scope.launch {
            // أرسل مرشّحات ICE المحلية إلى جهاز التحكم
            launch {
                manager.iceCandidates.collectLatest { candidate ->
                    signaling?.send(
                        Signal.Ice(
                            from = DeviceIdentifier.getOrGenerate(this@HostForegroundService),
                            to = "", // سيُملأ من قبل الخادم (الوجهة = المتصل)
                            sdp = candidate.sdp,
                            sdpMid = candidate.sdpMid,
                            sdpMLineIndex = candidate.sdpMLineIndex
                        )
                    )
                }
            }

            // عالج الطلبات الواردة من جهاز التحكم
            launch {
                manager.incomingRequests.collectLatest { request ->
                    handleRequest(request)
                }
            }

            // راقب حالة قناة البيانات
            launch {
                manager.state.collectLatest { peerState ->
                    serviceState.value = serviceState.value.copy(
                        peerState = peerState,
                        controllerConnected = peerState == PeerState.Connected
                    )
                }
            }
        }
    }

    /**
     * يعالج طلباً وارداً من جهاز التحكم ويبني الرد المناسب.
     */
    private suspend fun handleRequest(request: BridgeRequest) {
        val provider = dataProvider ?: return
        val manager = peerManager ?: return
        try {
            val data: JsonObject = when (request.cmd) {
                Commands.PING -> buildJsonObject {
                    put("pong", true)
                    put("time", System.currentTimeMillis())
                }

                Commands.DEVICE_INFO -> json.encodeToJsonElement(
                    DeviceInfo.serializer(),
                    provider.readDeviceInfo()
                ).jsonObject

                Commands.FILE_LIST -> {
                    val bucket = request.params?.get("bucket")?.jsonPrimitive?.content
                        ?: "images"
                    buildJsonObject {
                        put(
                            "items",
                            json.encodeToJsonElement(
                                ListSerializer(FileItem.serializer()),
                                provider.readFiles(bucket)
                            )
                        )
                    }
                }

                Commands.SMS_LIST -> buildJsonObject {
                    put(
                        "items",
                        json.encodeToJsonElement(
                            ListSerializer(SmsMessage.serializer()),
                            provider.readSms()
                        )
                    )
                }

                Commands.CONTACTS_LIST -> buildJsonObject {
                    put(
                        "items",
                        json.encodeToJsonElement(
                            ListSerializer(ContactItem.serializer()),
                            provider.readContacts()
                        )
                    )
                }

                Commands.CALLS_LIST -> buildJsonObject {
                    put(
                        "items",
                        json.encodeToJsonElement(
                            ListSerializer(CallLogItem.serializer()),
                            provider.readCallLog()
                        )
                    )
                }

                Commands.NOTIFICATIONS_LIST -> buildJsonObject {
                    put("items", "requires_notification_listener")
                }

                Commands.FILE_OPEN -> {
                    // تنزيل الملف يُدار بشكل منفصل عبر sendBinaryChunk
                    val fileId = request.params?.get("fileId")?.jsonPrimitive?.content?.toLong()
                    val bucket = request.params?.get("bucket")?.jsonPrimitive?.content
                        ?: "images"
                    if (fileId != null) {
                        sendFileToController(manager, request.id, fileId, bucket)
                    }
                    return
                }

                else -> buildJsonObject {
                    put("error", "أمر غير معروف: ${request.cmd}")
                }
            }
            manager.sendResponse(BridgeResponse(request.id, true, null, data))
        } catch (t: Throwable) {
            manager.sendResponse(
                BridgeResponse(request.id, false, t.message ?: "خطأ غير معروف", null)
            )
        }
    }

    /**
     * يرسل ملفاً كاملاً إلى جهاز التحكم عبر قطع ثنائية.
     */
    private suspend fun sendFileToController(
        manager: PeerConnectionManager,
        requestId: Int,
        fileId: Long,
        bucket: String
    ) {
        val provider = dataProvider ?: return
        val stream = provider.openFile(fileId, bucket) ?: run {
            manager.sendResponse(BridgeResponse(requestId, false, "تعذّر فتح الملف", null))
            return
        }
        stream.use { input ->
            val buffer = ByteArray(FrameProtocol.MAX_PAYLOAD)
            var seq = 0
            var totalSent = 0L
            // الرأس أولاً: اسم الملف وحجمه
            val header = buildJsonObject {
                put("fileId", fileId)
                put("started", true)
            }
            manager.sendResponse(BridgeResponse(requestId, true, null, header))
            while (true) {
                val read = input.read(buffer)
                if (read <= 0) break
                manager.sendBinaryChunk(requestId, seq, -1, buffer, 0, read)
                seq++
                totalSent += read
            }
            // رسالة ختامية
            manager.sendResponse(
                BridgeResponse(
                    requestId, true, null,
                    buildJsonObject {
                        put("completed", true)
                        put("size", totalSent)
                    }
                )
            )
        }
    }

    /**
     * يوقف كل شيء بأمان.
     */
    private fun stopHost() {
        requestJob?.cancel()
        peerManager?.close()
        peerManager = null
        signaling?.disconnect()
        signaling = null
        serviceState.value = serviceState.value.copy(running = false)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
