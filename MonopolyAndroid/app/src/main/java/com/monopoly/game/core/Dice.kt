package com.monopoly.game.core

import kotlin.random.Random

data class DiceResult(
    val dice1: Int,
    val dice2: Int,
    val total: Int = dice1 + dice2,
    val isDouble: Boolean = dice1 == dice2
)

class Dice(private val seed: Long = System.currentTimeMillis()) {
    private val random = Random(seed)

    fun roll(): DiceResult {
        val d1 = random.nextInt(1, 7)
        val d2 = random.nextInt(1, 7)
        return DiceResult(d1, d2)
    }

    fun forceRoll(d1: Int, d2: Int): DiceResult {
        return DiceResult(d1, d2)
    }
}
