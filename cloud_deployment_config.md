# دليل ونقاط تكوين الربط السحابي (Fly.io & Render) للتطبيق والأكواد

هذا الدليل يوضح خطوة بخطوة كيفية نشر تطبيقك، ملفاتك، وخوادم الذكاء الاصطناعي أو الـ Backend الخاص بك على منصتي السحاب الشهيرتين: **Fly.io** و **Render**.

---

## أولاً: تكوين النشر على Fly.io (ملف `fly.toml`)

منصة **Fly.io** ممتازة لتشغيل حاويات Docker، التطبيقات المبنية بـ Node.js/Python، أو خوادم الـ Backend التي تتطلب استجابة سريعة.

### 1. ملف التكوين الرئيسي (`fly.toml`)
أنشئ ملفاً باسم `fly.toml` في المجلد الجذر لمشروعك:

```toml
### FILE: fly.toml
app = "my-ai-bridge-app"
primary_region = "fra" # اختر أقرب منطقة (مثلاً فرانكفورت)

[build]
  # إذا كنت تستخدم Dockerfile
  dockerfile = "Dockerfile"

[http_service]
  internal_port = 8080
  force_https = true
  auto_stop_machines = true
  auto_start_machines = true
  min_machines_running = 0
  processes = ["app"]

[env]
  PORT = "8080"
  NODE_ENV = "production"

[[services.ports]]
  port = 80
  handlers = ["http"]

[[services.ports]]
  port = 443
  handlers = ["tls", "http"]

[http_service.concurrency]
  type = "requests"
  soft_limit = 200
  hard_limit = 250
```

### 2. خطوات النشر على Fly.io (عبر PowerShell):
```powershell
# 1. تثبيت FlyCTL وتسجيل الدخول
fly auth login

# 2. إطلاق التطبيق لأول مرة (إنشاء الإعدادات آلياً إن لم تكن موجودة)
fly launch --no-deploy

# 3. رفع الأكواد والنشر الفعلي
fly deploy

# 4. فتح التطبيق على المتصفح
fly open
```

---

## ثانياً: تكوين النشر على Render (ملف `render.yaml`)

منصة **Render** ممتازة وسهلة الاستخدام لتشغيل خدمات الـ Web Services، قواعد البيانات، والـ Static Sites مع طبقة مجانية ممتازة.

### 1. ملف التكوين الرئيسي (`render.yaml`)
أنشئ ملفاً باسم `render.yaml` في المجلد الجذر لمشروعك:

```yaml
### FILE: render.yaml
services:
  # 1. خدمة الـ Backend أو التطبيق الرئيسي
  - type: web
    name: ai-bridge-backend
    env: node
    region: frankfurt
    plan: free
    branch: main
    buildCommand: "npm install"
    startCommand: "node server.js"
    envVars:
      - key: NODE_ENV
        value: production
      - key: PORT
        value: 10000
      - key: GROQ_API_KEY
        sync: false # يتم إدخالها يدوياً في لوحة تحكم Render للأمان

  # 2. خدمة قاعدة البيانات (اختياري)
  - type: psql
    name: ai-database
    region: frankfurt
    plan: free
    databaseName: aidb
    databaseUser: aiuser
```

### 2. خطوات النشر على Render:
1. ارفع مشروعك إلى مستودع على **GitHub** أو **GitLab**.
2. سجل الدخول إلى لوحة تحكم [Render Dashboard](https://render.com).
3. اضغط على **New+** ثم اختر **Blueprint**.
4. اربط مستودع GitHub الخاص بك؛ سيقوم Render قراءة ملف `render.yaml` تلقائياً وبناء التطبيق ونشره فوراً.

---

## ثالثاً: ملف Dockerfile الموحد للنشر السحابي (Docker Deployment)
لكي يعمل تطبيقك بكفاءة على Fly.io أو Render، استخدم ملف `Dockerfile` التالي في جذر المشروع:

```dockerfile
### FILE: Dockerfile
# استخدام أحدث إصدار خفيف من Node.js
FROM node:20-alpine

# تحديد مجلد العمل داخل الحاوية
WORKDIR /app

# نسخ ملفات الحزم والتثبيت
COPY package*.json ./
RUN npm ci --production

# نسخ باقي أكواد التطبيق والمجلدات
COPY . .

# فتح البورت المطلوب
EXPOSE 8080

# أمر تشغيل التطبيق
CMD ["node", "server.js"]
```

---

## رابعاً: خيارات النسخ المباشر والمعاينة (Quick Copy Snippets)

### 1. ملف `fly.toml` (للنشر على Fly.io)
```toml
app = "my-ai-bridge-app"
primary_region = "fra"
[http_service]
  internal_port = 8080
  force_https = true
[env]
  PORT = "8080"
```

### 2. ملف `render.yaml` (للنشر على Render)
```yaml
services:
  - type: web
    name: ai-bridge-backend
    env: node
    plan: free
    buildCommand: "npm install"
    startCommand: "node server.js"
```

---
*تم توليد وتجهيز هذا التقرير والملفات السحابية لتسهيل ربط مشروعك بالسحابة بنجاح.*
