package com.monopoly.game.models

data class Player(
    val id: Int,
    val name: String,
    val color: Int,
    var cash: Int = 15000,
    var position: Int = 0,
    var isBankrupt: Boolean = false,
    var isAI: Boolean = false,
    val aiDifficulty: Int = 1,
    var inJail: Boolean = false,
    var turnsInJail: Int = 0,
    val ownedProperties: MutableList<Int> = mutableListOf(),
    var getOutOfJailCards: Int = 0
) {
    fun canAfford(amount: Int): Boolean = cash >= amount && !isBankrupt

    fun addCash(amount: Int) {
        cash += amount
    }

    fun subtractCash(amount: Int): Boolean {
        if (!canAfford(amount)) return false
        cash -= amount
        return true
    }

    fun transferTo(other: Player, amount: Int): Boolean {
        if (!canAfford(amount)) return false
        subtractCash(amount)
        other.addCash(amount)
        return true
    }

    fun ownsProperty(propertyId: Int): Boolean = ownedProperties.contains(propertyId)

    fun addProperty(propertyId: Int) {
        if (!ownedProperties.contains(propertyId)) {
            ownedProperties.add(propertyId)
        }
    }

    fun removeProperty(propertyId: Int) {
        ownedProperties.remove(propertyId)
    }

    fun getNetWorth(): Int {
        // TODO: 加上地产价值
        return cash
    }
}
