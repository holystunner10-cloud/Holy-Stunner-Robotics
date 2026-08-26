package com.example.data.model

data class TechNode(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val researchCost: Long,
    val isUnlocked: Boolean = false,
    val iconName: String,
    val unlockEffect: String
)

data class NewsItem(
    val id: String,
    val headline: String,
    val category: String,
    val isBreaking: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class EmergentEvent(
    val id: String,
    val title: String,
    val description: String,
    val affectedDistrictName: String,
    val categoryTarget: RobotCategory,
    val rewardBonus: Long,
    val durationSeconds: Int = 45,
    val iconName: String
)
