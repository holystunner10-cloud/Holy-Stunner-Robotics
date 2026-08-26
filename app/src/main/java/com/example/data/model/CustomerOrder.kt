package com.example.data.model

data class CustomerReview(
    val id: String,
    val customerName: String,
    val profession: String,
    val avatarEmoji: String,
    val robotDelivered: String,
    val ratingStars: Int, // 1 to 5
    val reviewText: String,
    val districtName: String,
    val tipAmount: Long,
    val timestamp: Long = System.currentTimeMillis()
)

data class CustomerOrder(
    val id: String,
    val citizenName: String,
    val profession: String,
    val avatarEmoji: String,
    val targetCategory: RobotCategory,
    val storyDescription: String,
    val districtId: String,
    val districtName: String,
    val paymentReward: Long,
    val bonusForHighGrade: Long,
    val requiredMinimumGrade: TestGrade = TestGrade.GRADE_C,
    val isFulfilled: Boolean = false,
    val fulfilledRobotId: String? = null,
    val generatedReview: CustomerReview? = null,
    val isVipOrder: Boolean = false
)
