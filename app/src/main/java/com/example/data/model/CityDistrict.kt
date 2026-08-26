package com.example.data.model

data class CityDistrict(
    val id: String,
    val name: String,
    val tagLine: String,
    val iconName: String,
    val population: Int,
    val aiTransformationPercent: Float, // 0f to 100f
    val aiCarsCount: Int = 0,
    val aiBikesCount: Int = 0,
    val deliveryDronesCount: Int = 0,
    val domesticBotsCount: Int = 0,
    val utilityBotsCount: Int = 0,
    val smartphonesCount: Int = 0,
    val smartTvsCount: Int = 0,
    val humanoidsCount: Int = 0,
    val citizenHappiness: Int = 75, // 0 to 100
    val trafficCongestionReduction: Int = 0, // 0 to 100%
    val cleanEnergyIndex: Int = 60, // 0 to 100
    val accentColorHex: Long = 0xFF00F5D4
) {
    val totalDeployedAiUnits: Int
        get() = aiCarsCount + aiBikesCount + deliveryDronesCount + domesticBotsCount + utilityBotsCount + smartphonesCount + smartTvsCount + humanoidsCount
}
