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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiBrainConfig
import com.example.data.model.RobotCategory
import com.example.data.model.RobotUnit
import com.example.data.repository.GameState
import com.example.ui.theme.*

@Composable
fun AiBrainLabScreen(
    gameState: GameState,
    onTrainPerk: (String, String, Long) -> Unit,
    onUpdateBrainConfig: (String, AiBrainConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val availableRobots = gameState.readyForDeliveryInventory

    var selectedRobot by remember(availableRobots) {
        mutableStateOf(availableRobots.firstOrNull())
    }

    var currentConfig by remember(selectedRobot) {
        mutableStateOf(selectedRobot?.brainConfig ?: AiBrainConfig())
    }

    val availablePerks = listOf(
        Triple("Michelin Recipe DB", "Fine Motor", 30L),
        Triple("Pedestrian Shield Matrix", "Safety", 40L),
        Triple("Alleyway Nav Matrix", "Pathfinding", 25L),
        Triple("Wind Shear Compensator", "Flight", 35L),
        Triple("Turing Empathy Core", "Language", 45L),
        Triple("Solar Grid Sync", "Energy", 30L)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("ai_brain_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Hero Header
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(CyberGold, CyberAmber)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Brain",
                            tint = Color(0xFF1A1000),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Holy Stunner Neural Matrix Lab",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGoldBright
                        )
                        Text(
                            text = "Program AI brains, calibrate neural weights & flash firmware perks",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Unit Selector
        if (availableRobots.isNotEmpty()) {
            item {
                Text(
                    text = "Select Robot Firmware to Flash & Tune",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(availableRobots) { bot ->
                        val isSelected = selectedRobot?.id == bot.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) CyberGold.copy(alpha = 0.15f) else DarkSurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) CyberGold else DarkSurfaceBorder
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    selectedRobot = bot
                                    currentConfig = bot.brainConfig
                                }
                                .testTag("select_brain_bot_${bot.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = getIconForCategory(bot.category),
                                    contentDescription = bot.customName,
                                    tint = if (isSelected) CyberGold else TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = bot.customName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TextPrimary else TextSecondary
                                    )
                                    Text(
                                        text = "${bot.brainConfig.overallIntelligenceRating} IQ",
                                        fontSize = 10.sp,
                                        color = CyberCyan
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        val activeBot = selectedRobot
        if (activeBot == null) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Manufacture a robot in the Factory to tune its neural brain!",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            // Brain Metrics Overview
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Overall IQ Rating", fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = "${currentConfig.overallIntelligenceRating} / 100",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGoldBright
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Safety Index", fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = "${currentConfig.safetyIndex}%",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Firmware OS", fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = currentConfig.firmwareVersion,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberCyan
                            )
                        }
                    }
                }
            }

            // Neural Weight Sliders
            item {
                Text(
                    text = "Neural Module Calibrations",
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        NeuralSliderRow(
                            title = "Vision & LiDAR Spatial Mesh",
                            value = currentConfig.visionLiDAR,
                            onValueChange = {
                                currentConfig = currentConfig.copy(visionLiDAR = it)
                                onUpdateBrainConfig(activeBot.id, currentConfig)
                            }
                        )
                        NeuralSliderRow(
                            title = "NLP & Voice Empathy Synthesis",
                            value = currentConfig.naturalLanguage,
                            onValueChange = {
                                currentConfig = currentConfig.copy(naturalLanguage = it)
                                onUpdateBrainConfig(activeBot.id, currentConfig)
                            }
                        )
                        NeuralSliderRow(
                            title = "Dynamic Pathfinding & Traffic AI",
                            value = currentConfig.pathfindingAI,
                            onValueChange = {
                                currentConfig = currentConfig.copy(pathfindingAI = it)
                                onUpdateBrainConfig(activeBot.id, currentConfig)
                            }
                        )
                        NeuralSliderRow(
                            title = "Fine Motor Dexterity & Precision",
                            value = currentConfig.fineMotorControl,
                            onValueChange = {
                                currentConfig = currentConfig.copy(fineMotorControl = it)
                                onUpdateBrainConfig(activeBot.id, currentConfig)
                            }
                        )
                        NeuralSliderRow(
                            title = "Asimov Safety & Ethical Matrix",
                            value = currentConfig.ethicsSafetyMatrix,
                            onValueChange = {
                                currentConfig = currentConfig.copy(ethicsSafetyMatrix = it)
                                onUpdateBrainConfig(activeBot.id, currentConfig)
                            }
                        )
                        NeuralSliderRow(
                            title = "Energy Conservation & Regenerative Kernel",
                            value = currentConfig.energyKernel,
                            onValueChange = {
                                currentConfig = currentConfig.copy(energyKernel = it)
                                onUpdateBrainConfig(activeBot.id, currentConfig)
                            }
                        )
                    }
                }
            }

            // Trainable Neural Perks
            item {
                Text(
                    text = "Specialized Neural Firmware Perks",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            items(availablePerks) { perk ->
                val isInstalled = activeBot.brainConfig.activePerks.contains(perk.first)
                val canAfford = gameState.researchPoints >= perk.third

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isInstalled) SuccessGreen.copy(alpha = 0.5f) else DarkSurfaceBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = perk.first,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Category: ${perk.second}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }

                        if (isInstalled) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SuccessGreen.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    onTrainPerk(activeBot.id, perk.first, perk.third)
                                },
                                enabled = canAfford,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                modifier = Modifier.testTag("install_perk_${perk.first}")
                            ) {
                                Text(
                                    text = "Train (${perk.third} RP)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00201C)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NeuralSliderRow(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
            Text(text = "Tier $value / 5", fontSize = 12.sp, color = CyberCyan, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3,
            colors = SliderDefaults.colors(
                thumbColor = CyberGold,
                activeTrackColor = CyberCyan,
                inactiveTrackColor = DarkSurfaceHighlight
            )
        )
    }
}
