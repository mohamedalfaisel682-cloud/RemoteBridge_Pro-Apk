# توثيق وهندسة نظام "جسر عن بعد" (RemoteBridge Pro)

## 1. نظرة عامة على المشروع والهوية
- **اسم التطبيق:** RemoteBridge Pro
- **معرّف الحزمة (Application ID):** `com.remotebridgepro`
- **مجلد العمل الرئيسي:** `F:\Ai\agent folders\RemoteBridge`
- **ملف APK الناتج:** `F:\Ai\agent folders\RemoteBridge\app\build\outputs\apk\debug\app-debug.apk` (الحجم: 68.1 ميجابايت، تاريخ البناء: 2026-10-18)
- **التقنيات المستخدمة:** Kotlin 2.1.0, Jetpack Compose, Material 3, WebRTC (Stream), OkHttp, Java-WebSocket, Room DB, DataStore, Go (Signaling Server), Docker, Render / Fly.io.

---

## 2. خريطة وهيكل الملفات والمجلدات الحقيقي
```
F:\Ai\agent folders\RemoteBridge\
├── app\                               # تطبيق الأندرويد
│   ├── build.gradle.kts               # إعدادات التبعيات و namespace (com.remotebridgepro)
│   └── src\main\
│       ├── AndroidManifest.xml        # الأذونات، الخدمات، الـ FileProvider
│       └── java\com\remotebridgepro\
│           ├── MainActivity.kt        # نقطة الدخول الرسومية
│           ├── BridgeApplication.kt   # حاوية قاعدة البيانات Room
│           ├── core\                  # النواة المشتركة
│           │   ├── model\Models.kt    # نماذج البيانات (File, Sms, Contact, etc.)
│           │   ├── signaling\         # عميل WebSocket والإشارات مع Reconnection
│           │   └── webrtc\            # محرك P2P وبث الشاشة الحية (MediaProjection)
│           ├── host\                  # الجهاز المضيف (القديم)
│           │   ├── provider\          # قراءة MediaStore, SMS, Contacts
│           │   └── service\           # Foreground Service & Notification Listener
│           ├── controller\            # جهاز التحكم (الجديد)
│           │   ├── ControllerManager  # مدير الاتصال بالمضيفين
│           │   └── RemoteRepository   # مستودع جلب البيانات البعيدة
│           └── ui\                    # واجهات Jetpack Compose
│               ├── navigation\        # مسارات التنقل (NavHost)
│               └── screens\           # الشاشات الست (Home, Host, Controller, Dashboard, Files, Settings)
├── server\                            # (اختياري محلياً)
├── main.go                            # خادم الإشارات (في الجذر)
├── go.mod                             # تعريفات وحدة Go وحزمة websocket v1.5.3
├── Dockerfile                         # حاوية النشر السحابي
├── render.yaml                        # إعدادات Render السحابية
├── fly.toml                           # إعدادات Fly.io السحابية
└── run-server.ps1                     # سكريبت تشغيل السيرفر محلياً
```

---

## 3. تفاصيل الأذونات الأمنية وقواعد البيانات
- **الأذونات المطلوبة في الأندرويد:** `INTERNET`, `FOREGROUND_SERVICE`, `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`, `READ_SMS`, `READ_CONTACTS`, `READ_CALL_LOG`.
- **قاعدة البيانات المحلية (Room):** `bridge.db` تحتوي على جداول التخزين المؤقت (`cached_files`, `cached_sms`) لاستعراض البيانات بدون اتصال.
- **التخزين المحلي (DataStore):** `bridge_settings` لحفظ عنوان الخادم واسم الجهاز ودور التشغيل.

---

## 4. خطوات التطوير الـ 5 المتقدمة (المضمنة في المعمارية)
1. **Clean Architecture + MVI StateFlow:** فصل طبقة العرض عن طبقة البيانات والشبكة بالكامل.
2. **ضغط ونقل الثنائيات (Chunking):** إرسال واستقبال الملفات عبر قنوات بيانات `DataChannel` مجزأة (16KB) لضمان عدم حدوث انقطاع.
3. **الاكتشاف التلقائي / المرونة المحلية:** دعم الاتصال المباشر عبر `ws://IP:10000/ws` عند انقطاع الإنترنت أو عبر `wss://URL/ws` سحابياً.
4. **التشفير والآمان:** استخدام بروتوكول DTLS مدمج في WebRTC وتشفير TLS لاتصال الـ WebSocket.
5. **إعادة الاتصال الذكي (Smart Reconnection):** آلية `Exponential Backoff` لإعادة ربط الإشارات تلقائياً عند تذبذب الشبكة.

---

## 5. خطوات البناء والتطوير من الصفر (خلاصة الأوامر)
1. **تشغيل السيرفر محلياً:**
   ```powershell
   cd "F:\Ai\agent folders\RemoteBridge"
   go run main.go
   ```
2. **بناء تطبيق الأندرويد:**
   ```powershell
   cd "F:\Ai\agent folders\RemoteBridge"
   $env:JAVA_HOME="C:\Program Files\Java\jdk-17"; $env:PATH="$env:JAVA_HOME\bin;$env:PATH"
   .\gradlew assembleDebug
   ```
3. **النشر على Render:**
   - رفع الملفات (`main.go`, `go.mod`, `Dockerfile`) إلى جذر المستودع على GitHub.
   - جعل `Root Directory` فارغاً في إعدادات Render.
   - تعيين متغير البيئة `PORT` على `10000`.
