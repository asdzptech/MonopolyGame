@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 🚀 GitHub 自动编译 APK 配置向导
echo ========================================
echo.
echo 这个脚本会帮助你快速设置 GitHub 自动编译
echo.
echo 步骤：
echo.
echo 1️⃣  首先，注册 GitHub 账号（如果没有
echo.
echo 2️⃣  打开 GitHub 网站：https://github.com
echo.
echo 3️⃣  点击右上角 "+" → "New repository"
echo    仓库名称: MonopolyGame
echo    选择 Public
echo    点击 Create repository
echo.
echo 4️⃣  把下面这几行命令复制出来，保存为git bash：
echo.
echo ========================================
echo    在 Git 命令：
echo ========================================
echo.
echo cd /d %CD%
echo git init
echo git add .
echo git commit -m "Initial commit: 大富翁游戏"
echo git branch -M main
echo git remote add origin https://github.com/你的用户名/MonopolyGame.git
echo git push -u origin main
echo.
echo ========================================
echo.
echo 5️⃣  执行完上面的命令后，访问你的 GitHub 仓库页面
echo.
echo 6️⃣  点击 Actions 标签，就能看到自动编译正在进行！
echo.
echo ⏰ 3-5分钟后就能下载编译好的 APK 了！
echo.
echo 📍 下载位置：Actions → 点击最新的 Workflow Run
echo                → 下拉找到 Artifacts
echo                → 点击 "大富翁游戏APK"
echo.
echo.
pause
echo 按任意键打开 GitHub 网站...
start https://github.com
echo.
echo.
echo 💡 提示：如果没有安装 Git，可以下载 Git 官网下载：
echo.
echo    https://git-scm.com/download/win
echo.
pause
