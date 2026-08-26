package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayPhase
import com.example.data.model.EmergentEvent
import com.example.data.model.NewsItem
import com.example.ui.theme.*

@Composable
fun TopBarStats(
    capital: Long,
    researchPoints: Long,
    cityAiPercent: Float,
    brandTier: String,
    reputation: Int,
    cityHype: Int,
    currentDay: Int,
    dayPhase: DayPhase,
    simulationSpeed: Int,
    onAdvanceDay: () -> Unit,
    onSetSpeed: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_bar_stats"),
        color = DarkSurface,
        tonalElevation = 6.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Header Row: Brand Logo + Day Phase + End Day Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Company Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(CyberGold, CyberAmber)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PrecisionManufacturing,
                            contentDescription = "Holy Stunner Logo",
                            tint = Color(0xFF1A1000),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HOLY STUNNER",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                letterSpacing = 1.2.sp,
                                color = CyberGoldBright
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = CyberPink.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, CyberPink)
                            ) {
                                Text(
                                    text = "CEO",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberPink,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = brandTier,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }

                // Day & Time Indicator + Next Day Action
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Day Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(dayPhase.bgHex),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (dayPhase) {
                                    DayPhase.MORNING -> Icons.Default.WbSunny
                                    DayPhase.AFTERNOON -> Icons.Default.LightMode
                                    DayPhase.EVENING -> Icons.Default.WbTwilight
                                    DayPhase.NIGHT -> Icons.Default.Bedtime
                                },
                                contentDescription = "Time",
                                tint = CyberGold,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Day $currentDay",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGoldBright
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Advance / Next Day Button
                    Button(
                        onClick = onAdvanceDay,
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("end_day_button"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberGoldDark,
                            contentColor = CyberGoldBright
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Next Day",
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Next Day",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Resource Metrics Bar: Capital ($2M+), RP, Reputation, City AI, Hype
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Capital Card ($2M+)
                StatMetricCard(
                    modifier = Modifier.weight(1.3f),
                    icon = Icons.Default.Paid,
                    iconTint = CyberNeonGreen,
                    label = "Capital",
                    value = "$${String.format("%,d", capital)}",
                    accentColor = CyberNeonGreen
                )

                // Compute Research Points
                StatMetricCard(
                    modifier = Modifier.weight(0.9f),
                    icon = Icons.Default.Bolt,
                    iconTint = CyberCyan,
                    label = "Research",
                    value = "${researchPoints} RP",
                    accentColor = CyberCyan
                )

                // City Transformation
                StatMetricCard(
                    modifier = Modifier.weight(0.9f),
                    icon = Icons.Default.LocationCity,
                    iconTint = CyberPink,
                    label = "AI City",
                    value = "${cityAiPercent.toInt()}%",
                    accentColor = CyberPink
                )

                // Brand Hype & Heat
                StatMetricCard(
                    modifier = Modifier.weight(0.9f),
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = CyberAmber,
                    label = "Hype",
                    value = "$cityHype/100",
                    accentColor = CyberAmber
                )
            }
        }
    }
}

@Composable
fun StatMetricCard(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(13.dp)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 9.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun NewsTickerBar(
    newsFeed: List<NewsItem>,
    activeEvent: EmergentEvent?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("news_ticker_bar"),
        color = Color(0xFF060A12),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (activeEvent != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CyberGold.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Event",
                            tint = CyberGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "EVENT: ${activeEvent.title}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGoldBright
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            // News Ticker Items
            newsFeed.take(4).forEach { news ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(if (news.isBreaking) DangerRed else CyberCyan)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = news.headline,
                        fontSize = 10.sp,
                        color = if (news.isBreaking) TextPrimary else TextSecondary,
                        fontWeight = if (news.isBreaking) FontWeight.SemiBold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}
