# توثيق وهندسة نظام "جسر عن بعد" (RemoteBridge Pro)
*(نسخة منسقة وموجهة للطباعة والتوثيق المؤسسي)*

---

## 1. الهوية والمعلومات التقنية
* **اسم النظام:** RemoteBridge Pro
* **رقم الإصدار:** 2.0.0
* **معرّف الحزمة (Package Name):** `com.remotebridgepro`
* **مجلد المشروع المحلي:** `F:\Ai\agent folders\RemoteBridge`
* **مسار حزمة التثبيت (APK):** `F:\Ai\agent folders\RemoteBridge\app\build\outputs\apk\debug\app-debug.apk`
* **حجم التطبيق:** 68.1 ميجابايت
* **تاريخ اخر بناء ناجح:** 2026-10-18 00:44:07

---

## 2. معمارية وهيكل المكونات (Architecture & Structure)

```
F:\Ai\agent folders\RemoteBridge\
├── app\                               # تطبيق أندرويد (Kotlin + Compose)
│   ├── build.gradle.kts               # التبعيات وإعدادات الـ namespace
│   └── src\main\
│       ├── AndroidManifest.xml        # الأذونات، الخدمات، و FileProvider
│       └── java\com\remotebridgepro\
│           ├── MainActivity.kt        # نشاط الإطلاق الرسومي
│           ├── BridgeApplication.kt   # نقطة تهيئة قاعدة البيانات Room
│           ├── core\                  # النواة الأساسية
│           │   ├── model\             # النماذج المشتركة (File, Sms, Contact, Device)
│           │   ├── signaling\         # عميل WebSocket لإدارة جلسات الإشارات
│           │   └── webrtc\            # محرك P2P وبث الشاشة المباشر (MediaProjection)
│           ├── host\                  # الجهاز المضيف (القديم)
│           │   ├── provider\          # مزود بيانات ContentResolver (صور، رسائل، مكالمات)
│           │   └── service\           # Foreground Service & Notification Listener
│           ├── controller\            # جهاز التحكم (الجديد)
│           │   ├── ControllerManager  # مدير الاتصال المباشر بالمضيفين
│           │   └── RemoteRepository   # مستودع جلب البيانات البعيدة
│           └── ui\                    # واجهات Jetpack Compose + Material 3
│               ├── navigation\        # نظام التنقل (NavHost)
│               └── screens\           # الشاشات الست المتكاملة
├── main.go                            # خادم الإشارات بلغة Go (في الجذر)
├── go.mod                             # إدارة الحزم والتبعيات (Gorilla WebSocket v1.5.3)
├── Dockerfile                         # حاوية التشغيل السحابي (Alpine + Go 1.22)
└── render.yaml / fly.toml             # إعدادات النشر السحابي التلقائي
```

---

## 3. تفاصيل الأذونات وقاعدة البيانات المحلية
* **الأذونات الحساسة:** `INTERNET`, `FOREGROUND_SERVICE`, `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`, `READ_SMS`, `READ_CONTACTS`, `READ_CALL_LOG`.
* **قاعدة البيانات المحلية (Room):** `bridge.db` تحتوي على جداول (`cached_files`, `cached_sms`) للتخزين المؤقت للبيانات المعروضة من الجهاز البعيد.
* **التخزين المفصل (DataStore):** `bridge_settings` لحفظ إعدادات الخادم وأسماء الأجهزة.

---

## 4. الخطوات الخمس المتقدمة لتطوير النظام
1. **Clean Architecture:** فصل تام لطبقات البيانات، الشبكة، والعرض.
2. **Chunking Protocol:** إرسال واستقبال الملفات الضخمة مقطعة عبر قنوات البيانات (`DataChannel`) بقطع 16KB لضمان الموثوقية.
3. **Hybrid Connectivity:** إمكانية التبديل المرن بين الشبكة المحلية (`ws://IP:10000/ws`) والسحابة (`wss://URL/ws`) عبر شاشة الإعدادات.
4. **End-to-End Encryption Readiness:** تشفير DTLS المدمج في WebRTC وتأمين قناة البيانات.
5. **Smart Reconnection:** خوارزمية تراجع أُسّي (`Exponential Backoff`) لإعادة الاتصال التلقائي عند تذبذب الشبكة.

---

## 5. خطوات البناء والتنفيذ الكاملة
1. **تشغيل الخادم محلياً (بدون إنترنت):**
   ```powershell
   cd "F:\Ai\agent folders\RemoteBridge"
   go run main.go
   ```
2. **بناء وتوليد تطبيق الأندرويد (APK):**
   ```powershell
   cd "F:\Ai\agent folders\RemoteBridge"
   $env:JAVA_HOME="C:\Program Files\Java\jdk-17"; $env:PATH="$env:JAVA_HOME\bin;$env:PATH"
   .\gradlew assembleDebug
   ```
3. **النشر السحابي (Render):**
   - رفع الملفات (`main.go`, `go.mod`, `Dockerfile`) إلى جذر المستودع على GitHub.
   - جعل `Root Directory` فارغاً في لوحة تحكم Render.
   - ضبط متغير البيئة `PORT` على القيمة `10000`.
