package com.monopoly.game.core

import com.monopoly.game.models.*
import android.graphics.Color

class GameBoard {
    val properties: List<Property> = createStandardBoard()

    private fun createStandardBoard(): List<Property> {
        return listOf(
            // 0 - GO
            Property(0, "起点", PropertyType.GO, color = Color.parseColor("#FFD700")),

            // 1 - 棕色地产1
            Property(1, "地中海大道", PropertyType.LAND, 600, listOf(20, 100, 300, 900, 1600, 2500), 500, 500, 0, Color.parseColor("#8B4513")),

            // 2 - 公共基金
            Property(2, "公共基金", PropertyType.COMMUNITY_CHEST, color = Color.parseColor("#FF69B4")),

            // 3 - 棕色地产2
            Property(3, "波罗的海大道", PropertyType.LAND, 600, listOf(40, 200, 600, 1800, 3200, 4500), 500, 500, 0, Color.parseColor("#8B4513")),

            // 4 - 所得税
            Property(4, "所得税", PropertyType.TAX, price = 2000, color = Color.parseColor("#696969")),

            // 5 - 铁路1
            Property(5, "阅读铁路", PropertyType.RAILROAD, 2000, color = Color.parseColor("#000000")),

            // 6 - 浅蓝色1
            Property(6, "东方大道", PropertyType.LAND, 1000, listOf(60, 300, 900, 2700, 4000, 5500), 500, 500, 1, Color.parseColor("#87CEEB")),

            // 7 - 机会
            Property(7, "机会", PropertyType.CHANCE, color = Color.parseColor("#FFA500")),

            // 8 - 浅蓝色2
            Property(8, "弗蒙特大道", PropertyType.LAND, 1000, listOf(60, 300, 900, 2700, 4000, 5500), 500, 500, 1, Color.parseColor("#87CEEB")),

            // 9 - 浅蓝色3
            Property(9, "康涅狄格大道", PropertyType.LAND, 1200, listOf(80, 400, 1000, 3000, 4500, 6000), 500, 500, 1, Color.parseColor("#87CEEB")),

            // 10 - 监狱
            Property(10, "监狱/探视", PropertyType.JAIL, color = Color.parseColor("#696969")),

            // 11 - 粉色1
            Property(11, "圣查尔斯广场", PropertyType.LAND, 1400, listOf(100, 500, 1500, 4500, 6250, 7500), 1000, 1000, 2, Color.parseColor("#FFB6C1")),

            // 12 - 电力公司
            Property(12, "电力公司", PropertyType.UTILITY, 1500, color = Color.parseColor("#FFFFFF")),

            // 13 - 粉色2
            Property(13, "各州大道", PropertyType.LAND, 1400, listOf(100, 500, 1500, 4500, 6250, 7500), 1000, 1000, 2, Color.parseColor("#FFB6C1")),

            // 14 - 粉色3
            Property(14, "弗吉尼亚大道", PropertyType.LAND, 1600, listOf(120, 600, 1800, 5000, 7000, 9000), 1000, 1000, 2, Color.parseColor("#FFB6C1")),

            // 15 - 铁路2
            Property(15, "宾夕法尼亚铁路", PropertyType.RAILROAD, 2000, color = Color.parseColor("#000000")),

            // 16 - 橙色1
            Property(16, "圣詹姆斯广场", PropertyType.LAND, 1800, listOf(140, 700, 2000, 5500, 7500, 9500), 1000, 1000, 3, Color.parseColor("#FFA500")),

            // 17 - 公共基金
            Property(17, "公共基金", PropertyType.COMMUNITY_CHEST, color = Color.parseColor("#FF69B4")),

            // 18 - 橙色2
            Property(18, "田纳西大道", PropertyType.LAND, 1800, listOf(140, 700, 2000, 5500, 7500, 9500), 1000, 1000, 3, Color.parseColor("#FFA500")),

            // 19 - 橙色3
            Property(19, "纽约大道", PropertyType.LAND, 2000, listOf(160, 800, 2200, 6000, 8000, 10000), 1000, 1000, 3, Color.parseColor("#FFA500")),

            // 20 - 免费停车
            Property(20, "免费停车", PropertyType.FREE_PARKING, color = Color.parseColor("#32CD32")),

            // 21 - 红色1
            Property(21, "肯塔基大道", PropertyType.LAND, 2200, listOf(180, 900, 2500, 7000, 8750, 10500), 1500, 1500, 4, Color.parseColor("#FF0000")),

            // 22 - 机会
            Property(22, "机会", PropertyType.CHANCE, color = Color.parseColor("#FFA500")),

            // 23 - 红色2
            Property(23, "印第安纳大道", PropertyType.LAND, 2200, listOf(180, 900, 2500, 7000, 8750, 10500), 1500, 1500, 4, Color.parseColor("#FF0000")),

            // 24 - 红色3
            Property(24, "伊利诺伊大道", PropertyType.LAND, 2400, listOf(200, 1000, 3000, 7500, 9250, 11000), 1500, 1500, 4, Color.parseColor("#FF0000")),

            // 25 - 铁路3
            Property(25, "B&O铁路", PropertyType.RAILROAD, 2000, color = Color.parseColor("#000000")),

            // 26 - 黄色1
            Property(26, "大西洋大道", PropertyType.LAND, 2600, listOf(220, 1100, 3300, 8000, 9750, 12000), 1500, 1500, 5, Color.parseColor("#FFFF00")),

            // 27 - 黄色2
            Property(27, "维恩图大道", PropertyType.LAND, 2600, listOf(220, 1100, 3300, 8000, 9750, 12000), 1500, 1500, 5, Color.parseColor("#FFFF00")),

            // 28 - 水厂
            Property(28, "水厂", PropertyType.UTILITY, 1500, color = Color.parseColor("#FFFFFF")),

            // 29 - 黄色3
            Property(29, "马文花园", PropertyType.LAND, 2800, listOf(240, 1200, 3600, 8500, 10250, 12000), 1500, 1500, 5, Color.parseColor("#FFFF00")),

            // 30 - 去监狱
            Property(30, "去监狱", PropertyType.GO_TO_JAIL, color = Color.parseColor("#696969")),

            // 31 - 绿色1
            Property(31, "太平洋大道", PropertyType.LAND, 3000, listOf(260, 1300, 3900, 9000, 11000, 12750), 2000, 2000, 6, Color.parseColor("#006400")),

            // 32 - 绿色2
            Property(32, "北卡罗来纳大道", PropertyType.LAND, 3000, listOf(260, 1300, 3900, 9000, 11000, 12750), 2000, 2000, 6, Color.parseColor("#006400")),

            // 33 - 公共基金
            Property(33, "公共基金", PropertyType.COMMUNITY_CHEST, color = Color.parseColor("#FF69B4")),

            // 34 - 绿色3
            Property(34, "宾夕法尼亚大道", PropertyType.LAND, 3200, listOf(280, 1500, 4500, 10000, 12000, 14000), 2000, 2000, 6, Color.parseColor("#006400")),

            // 35 - 铁路4
            Property(35, "短线铁路", PropertyType.RAILROAD, 2000, color = Color.parseColor("#000000")),

            // 36 - 机会
            Property(36, "机会", PropertyType.CHANCE, color = Color.parseColor("#FFA500")),

            // 37 - 深蓝色1
            Property(37, "公园广场", PropertyType.LAND, 3500, listOf(350, 1750, 5000, 11000, 13000, 15000), 2000, 2000, 7, Color.parseColor("#00008B")),

            // 38 - 豪华税
            Property(38, "豪华税", PropertyType.TAX, price = 1000, color = Color.parseColor("#696969")),

            // 39 - 深蓝色2
            Property(39, "木板路", PropertyType.LAND, 4000, listOf(500, 2000, 6000, 14000, 17000, 20000), 2000, 2000, 7, Color.parseColor("#00008B"))
        )
    }

    fun getPropertyAt(position: Int): Property? {
        val index = position % properties.size
        return properties.getOrNull(index)
    }

    fun getPropertyById(id: Int): Property? {
        return properties.find { it.id == id }
    }

    fun getPlayerProperties(playerId: Int): List<Property> {
        return properties.filter { it.ownerId == playerId }
    }

    fun getRailroadCount(playerId: Int): Int {
        return properties.count { it.type == PropertyType.RAILROAD && it.ownerId == playerId }
    }

    fun getUtilityCount(playerId: Int): Int {
        return properties.count { it.type == PropertyType.UTILITY && it.ownerId == playerId }
    }

    fun ownsMonopoly(colorGroup: Int, playerId: Int): Boolean {
        val groupProperties = properties.filter { it.colorGroup == colorGroup }
        return groupProperties.isNotEmpty() && groupProperties.all { it.ownerId == playerId && !it.isMortgaged }
    }
}
