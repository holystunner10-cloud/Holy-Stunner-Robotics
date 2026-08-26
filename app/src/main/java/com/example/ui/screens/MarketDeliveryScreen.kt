package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerOrder
import com.example.data.model.CustomerReview
import com.example.data.model.RobotUnit
import com.example.data.model.TestGrade
import com.example.data.repository.GameState
import com.example.ui.theme.*

@Composable
fun MarketDeliveryScreen(
    gameState: GameState,
    onDeliverOrder: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Citizen Orders, 1: Reviews Feed
    var selectedOrderForDelivery by remember { mutableStateOf<CustomerOrder?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("market_delivery_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Hero Summary Banner
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Citizen Market & Home Deliveries",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Deliver smart robots to everyday people & transform homes",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CyberGold.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold)
                    ) {
                        Text(
                            text = "${gameState.totalRobotsDelivered} Delivered",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGoldBright,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Tab Selector Row
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurfaceElevated,
                contentColor = CyberGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CyberGold
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Citizen Orders (${gameState.customerOrders.count { !it.isFulfilled }})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Citizen Reviews (${gameState.customerReviews.size})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }

        if (selectedTab == 0) {
            // Orders Tab
            val activeOrders = gameState.customerOrders.filter { !it.isFulfilled }
            if (activeOrders.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurfaceElevated,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "All current citizen orders fulfilled! New orders will arrive shortly.",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            } else {
                items(activeOrders) { order ->
                    val matchingRobots = gameState.readyForDeliveryInventory.filter {
                        it.category == order.targetCategory
                    }

                    CitizenOrderCard(
                        order = order,
                        availableMatchingCount = matchingRobots.size,
                        onFulfillClick = {
                            selectedOrderForDelivery = order
                        }
                    )
                }
            }
        } else {
            // Citizen Reviews Feed
            if (gameState.customerReviews.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurfaceElevated,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No reviews yet. Deliver robots to citizens to see their feedback!",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            } else {
                items(gameState.customerReviews) { review ->
                    CitizenReviewCard(review = review)
                }
            }
        }
    }

    // Modal to Select Robot to Deliver
    if (selectedOrderForDelivery != null) {
        val order = selectedOrderForDelivery!!
        val matchingRobots = gameState.readyForDeliveryInventory.filter {
            it.category == order.targetCategory
        }

        AlertDialog(
            onDismissRequest = { selectedOrderForDelivery = null },
            title = {
                Text(
                    text = "Dispatch Delivery to ${order.citizenName}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Destination: ${order.districtName}",
                        fontSize = 12.sp,
                        color = CyberCyan
                    )
                    Text(
                        text = "Select a manufactured ${order.targetCategory.title} from inventory:",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )

                    if (matchingRobots.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceHighlight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠️ No ${order.targetCategory.title} in inventory! Build one in the Factory first.",
                                fontSize = 12.sp,
                                color = WarningOrange,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        matchingRobots.forEach { robot ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = DarkSurfaceHighlight,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (robot.isTested) SuccessGreen.copy(alpha = 0.6f) else DarkSurfaceBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onDeliverOrder(order.id, robot.id)
                                        selectedOrderForDelivery = null
                                    }
                                    .testTag("select_deliver_bot_${robot.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = robot.customName,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "QA Grade: ${robot.testGrade.gradeLetter}",
                                            fontSize = 11.sp,
                                            color = Color(robot.testGrade.badgeColorHex)
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            onDeliverOrder(order.id, robot.id)
                                            selectedOrderForDelivery = null
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Deliver",
                                            color = Color(0xFF1A1000),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { selectedOrderForDelivery = null }) {
                    Text(text = "Close", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun CitizenOrderCard(
    order: CustomerOrder,
    availableMatchingCount: Int,
    onFulfillClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("order_card_${order.id}"),
        shape = RoundedCornerShape(14.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.avatarEmoji,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceHighlight)
                            .wrapContentSize(Alignment.Center)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = order.citizenName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${order.profession} • ${order.districtName}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CyberGold.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "$${order.paymentReward}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGoldBright,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "\"${order.storyDescription}\"",
                fontSize = 12.sp,
                color = TextSecondary,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )

            Divider(color = DarkSurfaceBorder)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getIconForCategory(order.targetCategory),
                        contentDescription = order.targetCategory.title,
                        tint = CyberCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Needs: ${order.targetCategory.title}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyberCyan
                    )
                }

                Button(
                    onClick = onFulfillClick,
                    enabled = availableMatchingCount > 0,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberGold,
                        disabledContainerColor = DarkSurfaceHighlight
                    ),
                    modifier = Modifier.testTag("fulfill_order_button_${order.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Dispatch",
                        tint = if (availableMatchingCount > 0) Color(0xFF1A1000) else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (availableMatchingCount > 0) "Dispatch ($availableMatchingCount Ready)" else "No Unit in Storage",
                        fontSize = 11.sp,
                        color = if (availableMatchingCount > 0) Color(0xFF1A1000) else TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CitizenReviewCard(
    review: CustomerReview,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = review.avatarEmoji,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = review.customerName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${review.profession} • ${review.districtName}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }

                Row {
                    repeat(review.ratingStars) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = CyberGold,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Text(
                text = review.reviewText,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Delivered: ${review.robotDelivered}",
                    fontSize = 10.sp,
                    color = CyberCyan
                )
                if (review.tipAmount > 0) {
                    Text(
                        text = "+$${review.tipAmount} Tip Received",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }
        }
    }
}
