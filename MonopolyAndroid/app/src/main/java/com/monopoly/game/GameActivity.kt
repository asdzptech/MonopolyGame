package com.monopoly.game

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.monopoly.game.core.GameEngine
import com.monopoly.game.core.GameState
import com.monopoly.game.ui.BoardView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var boardView: BoardView
    private lateinit var rollDiceButton: Button
    private lateinit var endTurnButton: Button
    private lateinit var buyButton: Button
    private lateinit var skipButton: Button
    private lateinit var backToMenuButton: Button
    private lateinit var turnInfoText: TextView
    private lateinit var playerInfoText: TextView
    private lateinit var messageText: TextView
    private lateinit var diceText: TextView

    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val playerCount = intent.getIntExtra("player_count", 3)
        val difficulty = intent.getIntExtra("ai_difficulty", 2)

        gameEngine = GameEngine(playerCount, difficulty)

        setupViews()
        setupBoard()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViews() {
        boardView = findViewById(R.id.boardView)
        rollDiceButton = findViewById(R.id.rollDiceButton)
        endTurnButton = findViewById(R.id.endTurnButton)
        buyButton = findViewById(R.id.buyButton)
        skipButton = findViewById(R.id.skipButton)
        backToMenuButton = findViewById(R.id.backToMenuButton)
        turnInfoText = findViewById(R.id.turnInfoText)
        playerInfoText = findViewById(R.id.playerInfoText)
        messageText = findViewById(R.id.messageText)
        diceText = findViewById(R.id.diceText)

        // 初始隐藏购买按钮
        buyButton.isEnabled = false
        buyButton.alpha = 0.5f
        skipButton.isEnabled = false
        skipButton.alpha = 0.5f
        endTurnButton.isEnabled = false
        endTurnButton.alpha = 0.5f
    }

    private fun setupBoard() {
        boardView.setGameBoard(gameEngine.getBoard())
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            gameEngine.uiState.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: com.monopoly.game.core.GameUiState) {
        // 更新回合信息
        turnInfoText.text = "回合 ${state.turnNumber} - ${state.players[state.currentPlayerIndex].name}"
        turnInfoText.setTextColor(state.players[state.currentPlayerIndex].color)

        // 更新玩家信息
        val playerInfo = state.players.filter { !it.isBankrupt }.joinToString("\n") {
            val status = if (it.inJail) " (狱中)" else ""
            "${it.name}: $${it.cash}${status}"
        }
        playerInfoText.text = playerInfo

        // 更新消息
        messageText.text = state.message

        // 更新骰子
        if (state.lastDiceResult != null) {
            diceText.text = "骰子: ${state.lastDiceResult!!.dice1} + ${state.lastDiceResult!!.dice2}"
        } else {
            diceText.text = ""
        }

        // 更新棋盘
        boardView.setPlayers(state.players)

        // 更新按钮状态
        updateButtonStates(state)

        // 检查游戏结束
        if (state.state == GameState.GAME_OVER) {
            showGameOverDialog(state.winnerIndex)
        }
    }

    private fun updateButtonStates(state: com.monopoly.game.core.GameUiState) {
        val isHumanTurn = !state.players[state.currentPlayerIndex].isAI && !state.players[state.currentPlayerIndex].isBankrupt

        when (state.state) {
            GameState.WAITING_FOR_ROLL -> {
                rollDiceButton.isEnabled = isHumanTurn
                rollDiceButton.alpha = if (isHumanTurn) 1.0f else 0.5f
                endTurnButton.isEnabled = false
                endTurnButton.alpha = 0.5f
                buyButton.isEnabled = false
                buyButton.alpha = 0.5f
                skipButton.isEnabled = false
                skipButton.alpha = 0.5f
            }
            GameState.WAITING_FOR_ACTION -> {
                rollDiceButton.isEnabled = false
                rollDiceButton.alpha = 0.5f
                endTurnButton.isEnabled = false
                endTurnButton.alpha = 0.5f
                buyButton.isEnabled = isHumanTurn
                buyButton.alpha = if (isHumanTurn) 1.0f else 0.5f
                skipButton.isEnabled = isHumanTurn
                skipButton.alpha = if (isHumanTurn) 1.0f else 0.5f
            }
            else -> {
                // 其他状态
                rollDiceButton.isEnabled = false
                rollDiceButton.alpha = 0.5f
                buyButton.isEnabled = false
                buyButton.alpha = 0.5f
                skipButton.isEnabled = false
                skipButton.alpha = 0.5f
            }
        }
    }

    private fun setupClickListeners() {
        rollDiceButton.setOnClickListener {
            if (!gameEngine.isCurrentPlayerAI()) {
                gameEngine.rollDice()
            }
        }

        buyButton.setOnClickListener {
            val currentPlayer = gameEngine.getCurrentPlayer()
            val position = currentPlayer?.position ?: -1
            val property = gameEngine.getBoard().getPropertyAt(position)
            if (currentPlayer != null && property != null) {
                gameEngine.buyProperty(currentPlayer, property)
            }
        }

        skipButton.setOnClickListener {
            gameEngine.skipPurchase()
        }

        endTurnButton.setOnClickListener {
            gameEngine.endTurn()
        }

        backToMenuButton.setOnClickListener {
            finish()
        }
    }

    private fun showGameOverDialog(winnerIndex: Int) {
        val winner = gameEngine.uiState.value.players[winnerIndex]
        AlertDialog.Builder(this)
            .setTitle("游戏结束！")
            .setMessage("恭喜 ${winner.name} 获胜！\n总资产: $${winner.getNetWorth()}")
            .setPositiveButton("再来一局") { _, _ ->
            gameEngine.resetGame()
            }
            .setNegativeButton("返回菜单") { _, _ ->
            finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("确认退出")
            .setMessage("确定要退出游戏吗？当前进度将丢失。")
            .setPositiveButton("确定") { _, _ ->
            super.onBackPressed()
            }
            .setNegativeButton("取消", null)
            .show()
    }
}
