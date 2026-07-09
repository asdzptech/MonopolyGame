# 🎲 大富翁 Android 游戏

完整原生 Android 实现的经典大富翁桌游，无需 Unity！纯 Kotlin 编写。

---

## ✨ 游戏特性

✅ **已实现功能**：
- 🎲 完整骰子系统 + 双倍检测
- 🏠 40格经典棋盘（含地产、铁路、公共设施）
- 💰 购买地产 + 收租机制
- 📊 完整经济系统
- 🤖 2-4人游戏（1人 + 1-3 AI）
- 🎯 三级 AI 难度
- 🚓 监狱系统
- 💸 税收 / 免费停车
- 🏗️ 建筑等级（房屋→酒店）
- 🎨 自定义棋盘视图（Canvas绘制）
- 📱 完整主菜单 + 游戏界面

🔄 **待实现**：
- 🃏 机会卡/公共基金卡系统
- 🤝 交易系统
- 💾 存档功能
- 🔊 音效

---

## 🛠️ 技术栈

| 技术 | 版本 |
|------|------|
| **语言** | Kotlin 1.9 |
| **最低SDK** | API 24 (Android 7.0) |
| **目标SDK** | API 34 (Android 14) |
| **构建工具** | Gradle 8.0 |
| **UI框架** | 原生View + 自定义Canvas |
| **架构** | MVVM + StateFlow |

---

## 📱 编译运行

### 方法一：使用 Android Studio（推荐）

1. **安装 Android Studio**（最新版即可）
2. **打开项目**：
   - 启动 Android Studio
   - 选择 "Open an Existing Project"
   - 选择 `MonopolyAndroid` 文件夹
3. **等待 Gradle 同步**（首次打开会自动下载依赖）
4. **连接 Android 手机** 或 **启动模拟器**
5. **点击 Run 按钮** ▶️

### 方法二：命令行编译（无需IDE）

**Windows:**
```bash
cd MonopolyAndroid
gradlew.bat assembleDebug
```

**生成的APK位置：**
```
MonopolyAndroid/app/build/outputs/apk/debug/app-debug.apk
```

**安装到手机：**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📁 项目结构

```
MonopolyAndroid/
├── app/
│   └── src/main/java/com/monopoly/game/
│       ├── core/                     # 核心游戏引擎
│       │   ├── GameEngine.kt         # 游戏主引擎
│       │   ├── GameBoard.kt          # 棋盘数据
│       │   └── Dice.kt               # 骰子系统
│       ├── models/                   # 数据模型
│       │   ├── Player.kt             # 玩家模型
│       │   ├── Property.kt           # 地产模型
│       │   └── Card.kt               # 卡片模型
│       ├── ui/                       # 自定义视图
│       │   └── BoardView.kt          # 棋盘自定义View
│       ├── MainActivity.kt           # 主菜单
│       └── GameActivity.kt           # 游戏界面
├── gradle/
│   └── wrapper/                      # Gradle wrapper
├── build.gradle                      # 项目级构建脚本
├── app/build.gradle                  # 应用级构建脚本
├── gradle.properties                 # Gradle配置
├── gradlew.bat                       # Windows Gradle wrapper
└── settings.gradle                   # Gradle设置
```

---

## 🎮 游戏玩法

### 基本操作

1. **选择配置**：
   - 玩家数量（2-4人）
   - AI难度（简单/普通/困难）

2. **回合流程**：
   - 点击「掷骰子」按钮
   - 棋子自动移动
   - 停留在空地：可选择「购买」或「跳过」
   - 停留在他人地产：自动支付租金
   - 停留在特殊格子：触发对应效果

3. **获胜条件**：
   - 其他所有玩家破产，仅剩一人时游戏结束

---

## 🎨 棋盘布局

标准大富翁棋盘（40格）：

- **底部 (0-10)**：起点 → 棕色地产 → 铁路 → 浅蓝色 → 监狱
- **左侧 (11-19)**：粉色 → 电力公司 → 橙色 → 铁路
- **顶部 (20-30)**：免费停车 → 红色 → 铁路 → 黄色 → 入狱
- **右侧 (31-39)**：绿色 → 水厂 → 铁路 → 深蓝色 → 起点

---

## ⚙️ 游戏平衡数值

| 项目 | 数值 |
|------|------|
| 初始现金 | $15,000 |
| 经过GO奖励 | $2,000 |
| 保释金 | $500 |
| 最便宜地产 | $600 |
| 最贵地产 | $4,000 |
| 最高酒店租金 | $20,000 |

---

## 🚀 开发计划

### Version 1.0（当前）
- ✅ 核心游戏引擎
- ✅ 完整棋盘渲染
- ✅ 基本AI对手
- ✅ 地产购买与收租
- ✅ 监狱系统

### Version 1.1
- 🃏 机会卡/公共基金系统
- 🏗️ 手动建造房屋/酒店
- 🤝 玩家间交易

### Version 1.2
- 💾 存档/读档
- 📊 游戏统计
- 🔊 音效与振动

---

## 📐 架构设计

### 核心设计模式

1. **单向数据流**：
   - GameEngine 维护单一状态源
   - UI 观察 StateFlow 自动更新

2. **状态机**：
   ```
   SETUP → WAITING_FOR_ROLL → MOVING → PROCESSING_TILE
         → WAITING_FOR_ACTION → END_TURN
   ```

3. **自定义视图**：
   - BoardView 继承自 View
   - 使用 Canvas 绘制棋盘和棋子
   - 高性能无依赖

---

## 📄 许可证

本项目仅供学习交流使用。

---

## 🎯 常见问题

**Q: 为什么不用Unity？**
A: 原生Android体积更小（APK约5MB vs Unity的50MB+），运行更快，无需额外游戏引擎。

**Q: 支持什么Android版本？**
A: 最低支持 Android 7.0 (API 24)，覆盖 99% 以上活跃设备。

**Q: 如何修改棋盘大小？**
A: 在 `BoardView.kt` 中修改 tileSize 计算逻辑即可。

---

**Enjoy the game! 🎲💰🏠**
