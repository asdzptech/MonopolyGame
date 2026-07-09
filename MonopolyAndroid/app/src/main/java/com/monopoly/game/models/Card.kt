package com.monopoly.game.models

data class Card(
    val id: Int,
    val name: String,
    val description: String,
    val type: CardType,
    val effectType: CardEffectType,
    val amount: Int = 0,
    val targetPosition: Int = -1,
    val holdable: Boolean = false
)

enum class CardType {
    CHANCE,
    COMMUNITY_CHEST
}

enum class CardEffectType {
    CASH_GAIN,
    CASH_LOSS,
    CASH_FROM_PLAYERS,
    CASH_TO_PLAYERS,
    TELEPORT,
    GO_TO_JAIL,
    GET_OUT_OF_JAIL_FREE,
    HOUSE_REPAIRS,
    BIRTHDAY,
    DIVIDEND
}
