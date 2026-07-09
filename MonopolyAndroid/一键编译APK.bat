@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 🎲 大富翁 APK 一键编译脚本
echo ========================================
echo.

REM 检查是否安装了 Java
echo [1/5] 检查 Java 环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 未找到 Java，请先安装 JDK 8 或更高版本
    echo 下载地址: https://adoptium.net/
    pause
    exit /b 1
)
echo ✅ Java 环境正常
echo.

REM 检查是否安装了 Android SDK
echo [2/5] 检查 Android SDK...
if not exist "%LOCALAPPDATA%\Android\Sdk" (
    if not exist "%ANDROID_HOME%" (
        echo ❌ 未找到 Android SDK
        echo 请先安装 Android Studio 或 Android Command Line Tools
        echo 下载地址: https://developer.android.com/studio
        pause
        exit /b 1
    )
)
echo ✅ Android SDK 正常
echo.

REM 开始编译
echo [3/5] 开始编译 Debug APK...
echo 这可能需要几分钟时间，请耐心等待...
echo.

call gradlew.bat --no-daemon assembleDebug

if %errorlevel% neq 0 (
    echo.
    echo ❌ 编译失败！
    echo.
    echo 常见问题解决：
    echo 1. 确保已安装 Android SDK Platform 34
    echo 2. 确保网络连接正常（Gradle需要下载依赖）
    echo 3. 首次运行会自动下载Gradle和依赖包
    echo.
    pause
    exit /b 1
)

echo.
echo [4/5] ✅ 编译成功！
echo.

REM 检查 APK 文件
echo [5/5] 查找生成的 APK 文件...
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo.
    echo ========================================
    echo ✨ APK 编译完成！
    echo ========================================
    echo.
    echo 📂 APK 文件位置:
    echo %CD%\app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 📦 文件大小:
    dir "app\build\outputs\apk\debug\app-debug.apk" | findstr "app-debug.apk"
    echo.
    echo 💡 安装到手机方法:
    echo 1. 启用手机 USB 调试
    echo 2. 手机连接电脑
    echo 3. 运行: adb install app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 或者直接将 APK 文件复制到手机安装即可！
    echo.

    REM 询问是否打开文件位置
    set /p openFolder=是否打开APK所在文件夹？(Y/N):
    if /i "%openFolder%"=="Y" (
        explorer "app\build\outputs\apk\debug"
    )
) else (
    echo ❌ 未找到生成的 APK 文件
)

echo.
pause
