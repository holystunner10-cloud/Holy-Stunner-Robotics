package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.data.model.Achievement
import com.example.data.model.AchievementCategory
import com.example.data.repository.GameState
import com.example.ui.theme.*

@Composable
fun AchievementsScreen(
    gameState: GameState,
    onClaimReward: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf<AchievementCategory?>(null) }

    val unlockedCount = gameState.achievements.count { it.isUnlocked }
    val totalCount = gameState.achievements.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("achievements_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Hero Trophy Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .testTag("achievements_hero_banner"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(DarkSurfaceElevated, Color(0xFF1E1402))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(CyberGold.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = "Trophy",
                                        tint = CyberGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "HOLY TROPHIES & MILESTONES",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.2.sp,
                                        color = CyberGoldBright
                                    )
                                    Text(
                                        text = "Tesla, SpaceX & Solo Tycoon Career",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CyberGold,
                                contentColor = Color(0xFF1A1000)
                            ) {
                                Text(
                                    text = "$unlockedCount / $totalCount",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Overall Progress Bar
                        LinearProgressIndicator(
                            progress = { if (totalCount > 0) unlockedCount.toFloat() / totalCount.toFloat() else 0f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = CyberGold,
                            trackColor = DarkSurfaceHighlight
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedCategoryFilter == null,
                        onClick = { selectedCategoryFilter = null },
                        label = { Text("All Trophies", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyberGold,
                            selectedLabelColor = Color(0xFF1A1000)
                        )
                    )
                }
                items(AchievementCategory.values()) { cat ->
                    FilterChip(
                        selected = selectedCategoryFilter == cat,
                        onClick = { selectedCategoryFilter = cat },
                        label = { Text(cat.displayName, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyberCyan,
                            selectedLabelColor = Color(0xFF041E26)
                        )
                    )
                }
            }
        }

        // Achievements List
        val filteredAchievements = gameState.achievements.filter {
            selectedCategoryFilter == null || it.category == selectedCategoryFilter
        }

        items(filteredAchievements) { ach ->
            AchievementCard(
                achievement = ach,
                onClaim = { onClaimReward(ach.id) }
            )
        }
    }
}

@Composable
private fun AchievementCard(
    achievement: Achievement,
    onClaim: () -> Unit
) {
    val isUnlocked = achievement.isUnlocked
    val isClaimed = achievement.isClaimed

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("achievement_card_${achievement.id}"),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                isClaimed -> DarkSurfaceBorder
                isUnlocked -> CyberGold.copy(alpha = 0.8f)
                else -> DarkSurfaceBorder
            }
        )
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
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (isUnlocked) CyberGold.copy(alpha = 0.2f) else DarkSurfaceHighlight
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getAchievementIcon(achievement.iconName),
                            contentDescription = achievement.title,
                            tint = if (isUnlocked) CyberGold else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = achievement.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) TextPrimary else TextSecondary
                        )
                        Text(
                            text = achievement.category.displayName,
                            fontSize = 10.sp,
                            color = CyberCyan
                        )
                    }
                }

                if (isClaimed) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = DarkSurfaceHighlight
                    ) {
                        Text(
                            text = "COMPLETED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else if (isUnlocked) {
                    Button(
                        onClick = onClaim,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color(0xFF1A1000)),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("claim_button_${achievement.id}")
                    ) {
                        Icon(imageVector = Icons.Default.Redeem, contentDescription = "Claim", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "CLAIM", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = "${achievement.currentProgress} / ${achievement.targetProgress}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }
            }

            Text(
                text = achievement.description,
                fontSize = 11.sp,
                color = TextSecondary
            )

            // Progress Bar
            if (!isClaimed) {
                LinearProgressIndicator(
                    progress = { achievement.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = if (isUnlocked) CyberNeonGreen else CyberCyan,
                    trackColor = DarkSurfaceHighlight
                )
            }

            // Reward Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reward: +$${String.format("%,d", achievement.rewardCapital)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberNeonGreen
                )
                Text(
                    text = "+${achievement.rewardRp} RP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan
                )
                Text(
                    text = "+${achievement.rewardReputation} Rep",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberPink
                )
            }
        }
    }
}

private fun getAchievementIcon(name: String): ImageVector {
    return when (name) {
        "Person" -> Icons.Default.Person
        "Paid" -> Icons.Default.Paid
        "PersonAdd" -> Icons.Default.PersonAdd
        "Groups" -> Icons.Default.Groups
        "Smartphone" -> Icons.Default.Smartphone
        "DirectionsCar" -> Icons.Default.DirectionsCar
        "RocketLaunch" -> Icons.Default.RocketLaunch
        "LocationCity" -> Icons.Default.LocationCity
        "AutoAwesome" -> Icons.Default.AutoAwesome
        "Star" -> Icons.Default.Star
        else -> Icons.Default.EmojiEvents
    }
}
