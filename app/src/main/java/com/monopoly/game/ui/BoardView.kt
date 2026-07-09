package com.monopoly.game.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.monopoly.game.core.GameBoard
import com.monopoly.game.models.Player
import com.monopoly.game.models.PropertyType
import kotlin.math.min

class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val boardPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val playerPaints = mutableListOf<Paint>()

    private var board: GameBoard? = null
    private var players: List<Player> = emptyList()

    private var boardSize: Float = 0f
    private var tileSize: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private val playerColors = listOf(
        Color.parseColor("#FF6B6B"),
        Color.parseColor("#4ECDC4"),
        Color.parseColor("#45B7D1"),
        Color.parseColor("#F7DC6F")
    )

    init {
        setupPaints()
    }

    private fun setupPaints() {
        boardPaint.style = Paint.Style.FILL
        boardPaint.color = Color.parseColor("#F5DEB3") // 小麦色棋盘

        borderPaint.style = Paint.Style.STROKE
        borderPaint.color = Color.BLACK
        borderPaint.strokeWidth = 2f

        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD

        playerColors.forEach { color ->
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = color
            paint.style = Paint.Style.FILL
            playerPaints.add(paint)
        }
    }

    fun setGameBoard(gameBoard: GameBoard) {
        board = gameBoard
        invalidate()
    }

    fun setPlayers(playerList: List<Player>) {
        players = playerList
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        boardSize = min(w, h).toFloat()
        tileSize = boardSize / 11f // 11格，两边各1格边距，中间9格空
        centerX = w / 2f
        centerY = h / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制棋盘背景
        canvas.drawRect(0f, 0f, boardSize, boardSize, boardPaint)

        // 绘制格子
        drawTiles(canvas)

        // 绘制玩家棋子
        drawPlayers(canvas)
    }

    private fun drawTiles(canvas: Canvas) {
        val properties = board?.properties ?: return

        for (i in 0 until 40) {
            val pos = getTilePosition(i)
            val property = properties[i]

            // 绘制格子背景
            boardPaint.color = property.color
            canvas.drawRect(pos.left, pos.top, pos.right, pos.bottom, boardPaint)

            // 绘制边框
            canvas.drawRect(pos.left, pos.top, pos.right, pos.bottom, borderPaint)

            // 绘制格子名称（只显示前4个字）
            textPaint.textSize = tileSize * 0.15f
            textPaint.color = Color.BLACK
            val shortName = if (property.name.length > 4) property.name.take(4) else property.name
            canvas.drawText(shortName, pos.centerX(), pos.centerY(), textPaint)

            // 绘制所有者颜色标记
            if (property.ownerId >= 0 && !property.isMortgaged) {
                val ownerColor = playerColors.getOrElse(property.ownerId) { Color.GRAY }
                boardPaint.color = ownerColor
                boardPaint.alpha = 100
                canvas.drawRect(pos.left, pos.top, pos.right, pos.top + tileSize * 0.15f, boardPaint)
                boardPaint.alpha = 255
            }

            // 绘制建筑等级
            if (property.buildingLevel > 0 && property.type == PropertyType.LAND) {
                drawBuildings(canvas, pos, property.buildingLevel)
            }
        }
    }

    private fun drawBuildings(canvas: Canvas, pos: RectF, level: Int) {
        val houseSize = tileSize * 0.12f
        val startX = pos.left + tileSize * 0.1f

        boardPaint.color = Color.parseColor("#228B22") // 绿色房子

        if (level < 5) {
            // 绘制房屋
            for (i in 0 until level) {
                canvas.drawRect(
                    startX + i * houseSize * 1.2f,
                    pos.bottom - houseSize - tileSize * 0.05f,
                    startX + i * houseSize * 1.2f + houseSize,
                    pos.bottom - tileSize * 0.05f,
                    boardPaint
                )
            }
        } else {
            // 绘制酒店（红色）
            boardPaint.color = Color.RED
            canvas.drawRect(
                startX,
                pos.bottom - houseSize * 1.5f - tileSize * 0.05f,
                startX + houseSize * 3f,
                pos.bottom - tileSize * 0.05f,
                boardPaint
            )
        }
    }

    private fun drawPlayers(canvas: Canvas) {
        val playerSize = tileSize * 0.3f

        players.forEachIndexed { index, player ->
            if (!player.isBankrupt) {
                val pos = getTilePosition(player.position)
                val offset = index * (playerSize * 0.5f)

                // 绘制圆形棋子
                canvas.drawCircle(
                    pos.centerX() + offset - tileSize * 0.2f,
                    pos.centerY() + tileSize * 0.2f,
                    playerSize / 2,
                    playerPaints[index]
                )

                // 绘制玩家编号
                textPaint.textSize = playerSize * 0.5f
                textPaint.color = Color.WHITE
                canvas.drawText(
                    "${index + 1}",
                    pos.centerX() + offset - tileSize * 0.2f,
                    pos.centerY() + tileSize * 0.2f + playerSize * 0.15f,
                    textPaint
                )
            }
        }
    }

    private fun getTilePosition(index: Int): RectF {
        val margin = tileSize

        return when {
            // 底部横排 (0-10)
            index <= 10 -> {
                val x = margin + (10 - index) * tileSize
                val y = boardSize - margin - tileSize
                RectF(x, y, x + tileSize, y + tileSize)
            }
            // 左侧竖排 (11-19)
            index <= 19 -> {
                val x = margin
                val y = margin + (19 - index) * tileSize
                RectF(x, y, x + tileSize, y + tileSize)
            }
            // 顶部横排 (20-30)
            index <= 30 -> {
                val x = margin + (index - 20) * tileSize
                val y = margin
                RectF(x, y, x + tileSize, y + tileSize)
            }
            // 右侧竖排 (31-39)
            else -> {
                val x = boardSize - margin - tileSize
                val y = margin + (index - 30) * tileSize
                RectF(x, y, x + tileSize, y + tileSize)
            }
        }
    }
}
