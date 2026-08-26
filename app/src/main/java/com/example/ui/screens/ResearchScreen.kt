package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TechNode
import com.example.data.repository.GameState
import com.example.ui.theme.*

@Composable
fun ResearchScreen(
    gameState: GameState,
    onUnlockTech: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("research_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Hero Header
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
                            text = "R&D Tech Tree & Giga-Upgrades",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Invest Research Compute (RP) to advance robotics technology",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CyberCyan.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
                    ) {
                        Text(
                            text = "${gameState.researchPoints} RP Available",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberCyan,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Tech Nodes List
        item {
            Text(
                text = "Innovation Breakthroughs",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        items(gameState.techNodes) { tech ->
            TechNodeCard(
                tech = tech,
                canAfford = gameState.researchPoints >= tech.researchCost,
                onUnlock = { onUnlockTech(tech.id) }
            )
        }

        // Corporate Milestones
        item {
            Text(
                text = "Holy Stunner Corporate Milestones",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MilestoneRow(
                        title = "Metropolis AI Pioneer",
                        desc = "Deliver 1st AI Robot to a citizen household",
                        isReached = gameState.totalRobotsDelivered >= 1
                    )
                    MilestoneRow(
                        title = "Autonomous Transit Pioneer",
                        desc = "Deploy AI Cars & Smart Bikes across 3 districts",
                        isReached = gameState.cityDistricts.count { it.aiCarsCount + it.aiBikesCount > 0 } >= 3
                    )
                    MilestoneRow(
                        title = "Smart City Visionary",
                        desc = "Reach 50% Citywide AI Transformation",
                        isReached = gameState.overallCityAiTransformation >= 50f
                    )
                }
            }
        }
    }
}

@Composable
fun TechNodeCard(
    tech: TechNode,
    canAfford: Boolean,
    onUnlock: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tech_card_${tech.id}"),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (tech.isUnlocked) SuccessGreen.copy(alpha = 0.5f) else DarkSurfaceBorder
        )
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
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (tech.isUnlocked) SuccessGreen.copy(alpha = 0.15f)
                                else CyberCyan.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getTechIcon(tech.iconName),
                            contentDescription = tech.title,
                            tint = if (tech.isUnlocked) SuccessGreen else CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = tech.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Branch: ${tech.category}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                if (tech.isUnlocked) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SuccessGreen.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "UNLOCKED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onUnlock,
                        enabled = canAfford,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberCyan,
                            disabledContainerColor = DarkSurfaceHighlight
                        ),
                        modifier = Modifier.testTag("unlock_tech_button_${tech.id}")
                    ) {
                        Text(
                            text = "Research (${tech.researchCost} RP)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (canAfford) Color(0xFF00201C) else TextMuted
                        )
                    }
                }
            }

            Text(
                text = tech.description,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = DarkSurfaceHighlight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Effect: ${tech.unlockEffect}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CyberGoldBright,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun MilestoneRow(
    title: String,
    desc: String,
    isReached: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isReached) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = title,
            tint = if (isReached) SuccessGreen else TextMuted,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isReached) TextPrimary else TextSecondary
            )
            Text(
                text = desc,
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}

fun getTechIcon(name: String): ImageVector {
    return when (name) {
        "Psychology" -> Icons.Default.Psychology
        "Air" -> Icons.Default.Air
        "Share" -> Icons.Default.Share
        "MenuBook" -> Icons.Default.MenuBook
        "Shield" -> Icons.Default.Shield
        "Factory" -> Icons.Default.PrecisionManufacturing
        else -> Icons.Default.Memory
    }
}
