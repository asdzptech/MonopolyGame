# 📱 无需安装 Android Studio 编译 APK 指南

以下提供 **3种最简单的方法**，都无需安装十几GB的Android Studio！

---

## 🚀 方法一：GitHub Actions 在线自动编译（⭐ 最推荐 ⭐）

**零环境配置，完全免费，5分钟搞定！**

### 步骤：

1. **注册/登录 GitHub**：https://github.com

2. **创建新仓库**：
   - 点击右上角 `+` → `New repository`
   - 名称：`MonopolyGame`
   - 选择 `Public`
   - 点击 `Create repository`

3. **上传全部代码**：
   - 把 `MonopolyAndroid` 文件夹内的所有文件拖到上传页面
   - 点击 `Commit changes`

4. **等待自动编译**：
   - 点击仓库的 `Actions` 标签页
   - 会看到 `Build Android APK` 工作流自动开始
   - 等待 3-5 分钟编译完成

5. **下载 APK**：
   - 点击完成的 Workflow Run
   - 在 `Artifacts` 区域下载 `app-debug.apk`

**✅ 优点：全程在线，零配置，5分钟出APK！**

---

## 🚀 方法二：安装命令行工具编译（推荐给电脑）

只需下载命令行工具（约300MB），无需完整IDE。

### Windows 步骤：

1. **下载 Android Command Line Tools**
   - 访问：https://developer.android.com/studio#command-tools
   - 下载 Windows 版本的 Command line tools only（约150MB）

2. **解压到文件夹**，例如 `C:\android-sdk`

3. **配置环境变量**：
   - 右键「此电脑」→ 属性 → 高级系统设置 → 环境变量
   - 新建系统变量 `ANDROID_HOME` = `C:\android-sdk`
   - 把 `%ANDROID_HOME%\platform-tools` 添加到 PATH

4. **安装必要的SDK组件**：
   ```cmd
   cd C:\android-sdk\cmdline-tools\latest\bin
   sdkmanager "platforms;android-34" "platform-tools" "build-tools;34.0.0"
   ```

5. **编译APK**：
   ```cmd
   cd MonopolyAndroid
   gradlew.bat assembleDebug
   ```

**APK位置**：`app\build\outputs\apk\debug\app-debug.apk`

---

## 🚀 方法三：手机端直接编译（用 Termux）

**用你的Android手机就能编译APK！**

1. **安装 Termux**：https://f-droid.org/packages/com.termux/

2. **在 Termux 中执行**：
   ```bash
   pkg update -y
   pkg install wget openjdk-17 git -y

   # 下载项目（或用手机传输文件）
   git clone https://github.com/你的用户名/MonopolyGame.git
   cd MonopolyGame

   # 给予执行权限
   chmod +x gradlew

   # 编译（手机约需10-15分钟）
   ./gradlew assembleDebug --no-daemon
   ```

---

## 📦 方法四：使用在线 APK 构建平台

### 方案 A：使用 AppBuilder

1. 访问 https://www.appypie.com/
2. 选择「Create Android App」
3. 上传项目文件，选择「Import Existing Code」
4. 等待在线编译
5. 下载生成的APK

### 方案 B：使用 Codeanywhere

1. 注册 Codeanywhere：https://codeanywhere.com
2. 创建 Android 开发环境容器
3. 上传项目代码
4. 运行 `./gradlew assembleDebug` 编译

---

## 🔧 GitHub Actions 自动编译配置（已创建）

我已经为你创建好了 `.github/workflows/build.yml` 文件，你只需要把代码上传到GitHub，就会**自动编译生成APK**！

**工作流会自动：**
- ✅ 配置 Java 17 环境
- ✅ 配置 Android SDK
- ✅ 缓存 Gradle 依赖（加速编译）
- ✅ 编译 Debug APK
- ✅ 自动上传 APK 到 Artifacts

---

## 💡 我可以帮你做的：

**如果你愿意把代码托管到GitHub**，我可以直接帮你配置好，你只需要：
1. 告诉我你的GitHub账号
2. 我配置好CI/CD
3. 你上传代码后就能收到自动编译好的APK链接

**或者，如果你愿意，** 我可以把代码整理成一个可以直接在手机上用AIDE编译的版本（手机端Android IDE）。

你想用哪种方案？ 😊
