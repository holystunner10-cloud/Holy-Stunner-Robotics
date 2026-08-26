package com.example.data.model

enum class AchievementCategory(val displayName: String, val iconName: String) {
    SOLO_PIONEER("Solo Pioneer", "Person"),
    TECH_TITAN("Tech Titan & Staff", "Groups"),
    PRODUCT_EMPIRE("Tesla & SpaceX Empire", "RocketLaunch"),
    METROPOLIS_AI("AI City Revolution", "LocationCity"),
    BILLIONAIRE_CLUB("Tycoon Capital", "Paid")
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val category: AchievementCategory,
    val iconName: String,
    val rewardCapital: Long,
    val rewardRp: Long,
    val rewardReputation: Int,
    val currentProgress: Int,
    val targetProgress: Int,
    val isUnlocked: Boolean = false,
    val isClaimed: Boolean = false
) {
    val progressFraction: Float
        get() = (currentProgress.toFloat() / targetProgress.toFloat()).coerceIn(0f, 1f)
}
