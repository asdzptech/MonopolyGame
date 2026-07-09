package com.monopoly.game.models

import android.graphics.Color

data class Property(
    val id: Int,
    val name: String,
    val type: PropertyType,
    val price: Int = 0,
    val rent: List<Int> = listOf(0, 0, 0, 0, 0, 0),
    val housePrice: Int = 0,
    val hotelPrice: Int = 0,
    val colorGroup: Int = 0,
    val color: Int = Color.GRAY,
    var ownerId: Int = -1,
    var buildingLevel: Int = 0,
    var isMortgaged: Boolean = false
) {
    fun isOwned(): Boolean = ownerId != -1 && !isMortgaged

    fun canBuildHouse(): Boolean {
        return type == PropertyType.LAND && isOwned() && buildingLevel < 4
    }

    fun canBuildHotel(): Boolean {
        return type == PropertyType.LAND && isOwned() && buildingLevel == 4
    }

    fun getCurrentRent(): Int {
        return when (type) {
            PropertyType.LAND -> rent[buildingLevel]
            PropertyType.RAILROAD -> 250 * (1 shl buildingLevel)
            PropertyType.UTILITY -> 0 // 骰子点数 * 乘数
            else -> 0
        }
    }
}

enum class PropertyType {
    GO,
    LAND,
    RAILROAD,
    UTILITY,
    CHANCE,
    COMMUNITY_CHEST,
    TAX,
    JAIL,
    FREE_PARKING,
    GO_TO_JAIL
}
