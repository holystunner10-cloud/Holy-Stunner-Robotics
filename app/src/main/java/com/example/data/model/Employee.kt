package com.example.data.model

enum class Department(val displayName: String, val iconName: String, val description: String) {
    HARDWARE_ASSEMBLY("Hardware Assembly", "PrecisionManufacturing", "Accelerates production speed of robots, cars, phones, bikes & TVs"),
    AI_RESEARCH("AI Brain Lab & Neural R&D", "Psychology", "Generates daily Research Points & unlocks higher base AI intelligence"),
    QA_TESTING("Quality Assurance & Testing", "Science", "Increases QA test pass rate and quality grade multipliers"),
    SALES_MARKETING("Sales & Public Relations", "Campaign", "Brings higher-paying VIP orders, boosts brand reputation & city hype")
}

data class Employee(
    val id: String,
    val name: String,
    val roleTitle: String,
    val avatarEmoji: String,
    val department: Department,
    val level: Int = 1,
    val skillRating: Int = 75,       // 50 to 100
    val dailySalary: Long,           // Paid per day
    val productivityBoostPercent: Int, // e.g. +15%
    val morale: Int = 100,           // 0 to 100
    val traitDescription: String,
    val isFounder: Boolean = false
) {
    val upgradeCost: Long
        get() = (level * 2500L) + (dailySalary * 2)
}

enum class HqTier(
    val title: String,
    val capacity: Int,
    val upgradeCost: Long,
    val dailyMaintenance: Long,
    val iconName: String,
    val description: String
) {
    SUBURBAN_GARAGE(
        title = "Suburban Garage Tech Lab",
        capacity = 2,
        upgradeCost = 0L,
        dailyMaintenance = 250L,
        iconName = "Home",
        description = "Where visionary giants start. A cramped garage with soldering irons and single server rack."
    ),
    INNOVATION_WAREHOUSE(
        title = "Industrial Tech Warehouse",
        capacity = 8,
        upgradeCost = 150000L,
        dailyMaintenance = 2500L,
        iconName = "Warehouse",
        description = "High-ceiling fabrication space with robotic crane tracks and dedicated team cubicles."
    ),
    SILICON_VALLEY_CAMPUS(
        title = "Metropolis AI Innovation Campus",
        capacity = 20,
        upgradeCost = 650000L,
        dailyMaintenance = 12000L,
        iconName = "Business",
        description = "Sleek glass-and-steel headquarters with cleanrooms, supercomputers and rooftop drone heliport."
    ),
    GIGAFACTORY_STARBASE(
        title = "Gigafactory Starbase Megacomplex",
        capacity = 50,
        upgradeCost = 2500000L,
        dailyMaintenance = 45000L,
        iconName = "Apartment",
        description = "Colossal industrial complex inspired by Tesla & SpaceX with sub-orbital rocket pads & automated lines."
    )
}

data class CeoProfile(
    val founderName: String = "Elon Vance",
    val title: String = "Chief Engineer & Visionary Founder",
    val avatarEmoji: String = "🕶️",
    val engineeringSkill: Int = 95,
    val charismaRating: Int = 90,
    val visionLevel: Int = 1,
    val soloCraftingBonus: Float = 1.35f,
    val currentHqTier: HqTier = HqTier.SUBURBAN_GARAGE
)
