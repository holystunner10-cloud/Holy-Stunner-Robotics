package com.example.data.model

enum class DayPhase(val title: String, val timeString: String, val iconName: String, val bgHex: Long) {
    MORNING("Morning Shift", "08:00 AM", "WbSunny", 0xFF0D1B2A),
    AFTERNOON("Peak Operations", "01:30 PM", "LightMode", 0xFF14213D),
    EVENING("Twilight Dispatch", "06:45 PM", "WbTwilight", 0xFF1F1235),
    NIGHT("Overnight Neural Training", "11:30 PM", "Bedtime", 0xFF080C14)
}

data class DailyReport(
    val dayNumber: Int,
    val grossRevenue: Long,
    val payrollPaid: Long,
    val maintenancePaid: Long,
    val netProfit: Long,
    val researchEarned: Long,
    val productsDeliveredCount: Int,
    val cityAiGrowth: Float,
    val citizenReviewHighlight: String,
    val dailyNewsHeadline: String
)

data class CeoBuff(
    val id: String,
    val name: String,
    val description: String,
    val durationDays: Int,
    val boostType: String
)
