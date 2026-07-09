package com.monopoly.game.core

import com.monopoly.game.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.graphics.Color

enum class GameState {
    SETUP,
    PLAYING,
    PLAYER_TURN_START,
    WAITING_FOR_ROLL,
    MOVING,
    PROCESSING_TILE,
    WAITING_FOR_ACTION,
    GAME_OVER
}

data class GameUiState(
    val state: GameState = GameState.SETUP,
    val currentPlayerIndex: Int = 0,
    val turnNumber: Int = 1,
    val players: List<Player> = emptyList(),
    val lastDiceResult: DiceResult? = null,
    val message: String = "欢迎来到大富翁！",
    val winnerIndex: Int = -1
)

class GameEngine(
    private val playerCount: Int = 3,
    private val aiDifficulty: Int = 2
) {
    private val board = GameBoard()
    private val dice = Dice()

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val players: MutableList<Player> = mutableListOf()
    private var consecutiveDoubles = 0

    private val playerColors = listOf(
        Color.parseColor("#FF6B6B"), // 红
        Color.parseColor("#4ECDC4"), // 青
        Color.parseColor("#45B7D1"), // 蓝
        Color.parseColor("#F7DC6F")  // 黄
    )

    private val playerNames = listOf("玩家", "AI-1", "AI-2", "AI-3")

    init {
        initializeGame()
    }

    private fun initializeGame() {
        players.clear()

        for (i in 0 until playerCount) {
            val isAI = i > 0
            players.add(
                Player(
                    id = i,
                    name = playerNames[i],
                    color = playerColors[i],
                    cash = 15000,
                    isAI = isAI,
                    aiDifficulty = if (isAI) aiDifficulty else 0
                )
            )
        }

        _uiState.value = GameUiState(
            state = GameState.PLAYER_TURN_START,
            currentPlayerIndex = 0,
            turnNumber = 1,
            players = players.toList(),
            message = "${players[0].name} 的回合，请掷骰子！"
        )
    }

    fun getBoard(): GameBoard = board

    fun rollDice() {
        val currentState = _uiState.value
        if (currentState.state != GameState.WAITING_FOR_ROLL) return

        val currentPlayer = players[currentState.currentPlayerIndex]

        // 在监狱中
        if (currentPlayer.inJail) {
            handleJailTurn(currentPlayer)
            return
        }

        val result = dice.roll()

        _uiState.value = _uiState.value.copy(
            lastDiceResult = result,
            message = "掷出了 ${result.dice1} + ${result.dice2} = ${result.total} ${if (result.isDouble) "(双倍!)" else ""}",
            state = GameState.MOVING
        )

        // 检查连续双倍
        if (result.isDouble) {
            consecutiveDoubles++
            if (consecutiveDoubles >= 3) {
                sendToJail(currentPlayer)
                return
            }
        } else {
            consecutiveDoubles = 0
        }

        // 移动玩家
        movePlayer(currentPlayer, result.total)
    }

    private fun handleJailTurn(player: Player) {
        // 简化：支付保释金出狱
        if (player.canAfford(500)) {
            player.subtractCash(500)
            player.inJail = false
            player.turnsInJail = 0
            _uiState.value = _uiState.value.copy(
                message = "${player.name} 支付了 $500 保释金出狱",
                players = players.toList()
            )
            // 可以继续回合
            _uiState.value = _uiState.value.copy(state = GameState.WAITING_FOR_ROLL)
        } else {
            player.turnsInJail++
            if (player.turnsInJail >= 3) {
                // 强制破产
                declareBankruptcy(player)
            } else {
                _uiState.value = _uiState.value.copy(
                    message = "${player.name} 在监狱中度过了 ${player.turnsInJail} 回合",
                    players = players.toList()
                )
                endTurn()
            }
        }
    }

    private fun movePlayer(player: Player, steps: Int) {
        val oldPosition = player.position
        var newPosition = (oldPosition + steps) % 40

        // 经过GO
        if (oldPosition + steps >= 40) {
            player.addCash(2000)
            _uiState.value = _uiState.value.copy(
                message = "${player.name} 经过起点，获得 $2000！",
                players = players.toList()
            )
        }

        player.position = newPosition
        _uiState.value = _uiState.value.copy(players = players.toList())

        // 处理格子
        android.os.Handler().postDelayed({
            processTile(player, newPosition)
        }, 1000)
    }

    private fun processTile(player: Player, position: Int) {
        val property = board.getPropertyAt(position) ?: return

        _uiState.value = _uiState.value.copy(
            state = GameState.PROCESSING_TILE,
            message = "${player.name} 到达了 ${property.name}"
        )

        android.os.Handler().postDelayed({
            when (property.type) {
                PropertyType.GO -> {
                    endTurn()
                }
                PropertyType.LAND -> {
                    processLandTile(player, property)
                }
                PropertyType.RAILROAD -> {
                    processLandTile(player, property)
                }
                PropertyType.UTILITY -> {
                    processLandTile(player, property)
                }
                PropertyType.CHANCE -> {
                    // TODO: 卡片系统
                    _uiState.value = _uiState.value.copy(message = "抽到了机会卡！(待实现)")
                    endTurn()
                }
                PropertyType.COMMUNITY_CHEST -> {
                    // TODO: 卡片系统
                    _uiState.value = _uiState.value.copy(message = "抽到了公共基金卡！(待实现)")
                    endTurn()
                }
                PropertyType.TAX -> {
                    val tax = property.price
                    player.subtractCash(tax)
                    _uiState.value = _uiState.value.copy(
                        message = "${player.name} 支付了 $tax 税金",
                        players = players.toList()
                    )
                    checkBankruptcy(player)
                    endTurn()
                }
                PropertyType.JAIL -> {
                    _uiState.value = _uiState.value.copy(message = "${player.name} 只是探访监狱")
                    endTurn()
                }
                PropertyType.FREE_PARKING -> {
                    _uiState.value = _uiState.value.copy(message = "${player.name} 在免费停车场休息")
                    endTurn()
                }
                PropertyType.GO_TO_JAIL -> {
                    sendToJail(player)
                }
            }
        }, 500)
    }

    private fun processLandTile(player: Player, property: Property) {
        if (!property.isOwned()) {
            // 可以购买
            if (player.canAfford(property.price) && !player.isAI) {
                _uiState.value = _uiState.value.copy(
                    state = GameState.WAITING_FOR_ACTION,
                    message = "是否花费 ${property.price} 购买 ${property.name}？"
                )
                // 给人类玩家选择机会，AI自动购买
            } else if (player.canAfford(property.price) && player.isAI) {
                // AI简单决策：70%概率购买
                if (Math.random() < 0.7) {
                    buyProperty(player, property)
                } else {
                    _uiState.value = _uiState.value.copy(message = "${player.name} 放弃购买 ${property.name}")
                    endTurn()
                }
            } else {
                _uiState.value = _uiState.value.copy(message = "${player.name} 买不起 ${property.name}")
                endTurn()
            }
        } else if (property.ownerId != player.id && !property.isMortgaged) {
            // 支付租金
            val owner = players.find { it.id == property.ownerId }
            if (owner != null) {
                val rent = property.getCurrentRent()
                if (player.transferTo(owner, rent)) {
                    _uiState.value = _uiState.value.copy(
                        message = "${player.name} 向 ${owner.name} 支付了 $rent 租金",
                        players = players.toList()
                    )
                    checkBankruptcy(player)
                } else {
                    declareBankruptcy(player)
                }
                endTurn()
            }
        } else {
            endTurn()
        }
    }

    fun buyProperty(player: Player, property: Property) {
        if (player.subtractCash(property.price)) {
            property.ownerId = player.id
            player.addProperty(property.id)

            _uiState.value = _uiState.value.copy(
                message = "${player.name} 购买了 ${property.name}！",
                players = players.toList()
            )
        }
        endTurn()
    }

    fun skipPurchase() {
        endTurn()
    }

    private fun sendToJail(player: Player) {
        player.position = 10
        player.inJail = true
        player.turnsInJail = 0
        consecutiveDoubles = 0

        _uiState.value = _uiState.value.copy(
            message = "${player.name} 被送进了监狱！",
            players = players.toList()
        )
        endTurn()
    }

    private fun checkBankruptcy(player: Player): Boolean {
        if (player.cash < 0) {
            declareBankruptcy(player)
            return true
        }
        return false
    }

    private fun declareBankruptcy(player: Player) {
        player.isBankrupt = true
        player.ownedProperties.forEach { propId ->
            board.getPropertyById(propId)?.ownerId = -1
        }
        player.ownedProperties.clear()

        _uiState.value = _uiState.value.copy(
            message = "${player.name} 破产了！",
            players = players.toList()
        )

        // 检查游戏是否结束
        val alivePlayers = players.filter { !it.isBankrupt }
        if (alivePlayers.size == 1) {
            val winner = alivePlayers[0]
            _uiState.value = _uiState.value.copy(
                state = GameState.GAME_OVER,
                winnerIndex = winner.id,
                message = "游戏结束！${winner.name} 获胜！总资产：${winner.getNetWorth()}"
            )
        }
    }

    fun endTurn() {
        val currentState = _uiState.value

        if (players.filter { !it.isBankrupt }.size == 1) {
            return // 游戏已结束
        }

        // 找到下一个未破产的玩家
        var nextIndex = (currentState.currentPlayerIndex + 1) % players.size
        while (players[nextIndex].isBankrupt) {
            nextIndex = (nextIndex + 1) % players.size
        }

        val nextTurn = if (nextIndex == 0) currentState.turnNumber + 1 else currentState.turnNumber

        val nextPlayer = players[nextIndex]
        _uiState.value = _uiState.value.copy(
            state = GameState.WAITING_FOR_ROLL,
            currentPlayerIndex = nextIndex,
            turnNumber = nextTurn,
            message = "${nextPlayer.name} 的回合，准备掷骰子！"
        )

        // AI自动掷骰子
        if (nextPlayer.isAI && !nextPlayer.isBankrupt) {
            android.os.Handler().postDelayed({
                rollDice()
            }, 1500)
        }
    }

    fun getCurrentPlayer(): Player? {
        return players.getOrNull(_uiState.value.currentPlayerIndex)
    }

    fun isCurrentPlayerAI(): Boolean {
        return getCurrentPlayer()?.isAI ?: false
    }

    fun resetGame() {
        consecutiveDoubles = 0
        initializeGame()
    }
}
