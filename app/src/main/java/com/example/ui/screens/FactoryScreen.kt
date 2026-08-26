package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ProductCategoryGroup
import com.example.data.model.RobotCategory
import com.example.data.model.RobotUnit
import com.example.data.model.TestGrade
import com.example.data.repository.GameState
import com.example.ui.theme.*

@Composable
fun FactoryScreen(
    gameState: GameState,
    onStartBuild: (RobotCategory, String) -> Unit,
    onNavigateToTesting: (RobotUnit) -> Unit,
    onNavigateToDelivery: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showBlueprintDialog by remember { mutableStateOf(false) }
    var selectedCategoryForBuild by remember { mutableStateOf(RobotCategory.CHEF_DOMESTIC) }
    var customRobotName by remember { mutableStateOf("") }
    var selectedGroupFilter by remember { mutableStateOf<ProductCategoryGroup?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("factory_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Hero Factory Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_factory_lab),
                        contentDescription = "Holy Stunner Factory Cleanroom",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, DarkBackground.copy(alpha = 0.90f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CyberGold.copy(alpha = 0.9f)
                            ) {
                                Text(
                                    text = gameState.ceoProfile.currentHqTier.title.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF1A1000),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            if (gameState.isSoloFounder) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CyberPink.copy(alpha = 0.85f)
                                ) {
                                    Text(
                                        text = "⚡ SOLO FOUNDER +35% SPEED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Holy Stunner Manufacturing Lab",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Bays Active: ${gameState.buildingRobots.size} / ${gameState.factorySlotsTotal} Capacity • Staff: ${gameState.employees.size} / ${gameState.ceoProfile.currentHqTier.capacity}",
                            fontSize = 11.sp,
                            color = CyberCyan
                        )
                    }
                }
            }
        }

        // Product Line Filter Tabs (Robots, Phones, Cars/Bikes, TVs, Drones)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Product Lines (Robots, Phones, Cars, TVs, Drones)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedGroupFilter == null,
                            onClick = { selectedGroupFilter = null },
                            label = { Text("All Products", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberGold,
                                selectedLabelColor = Color(0xFF1A1000)
                            )
                        )
                    }
                    items(ProductCategoryGroup.values()) { group ->
                        FilterChip(
                            selected = selectedGroupFilter == group,
                            onClick = { selectedGroupFilter = group },
                            label = { Text(group.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan,
                                selectedLabelColor = Color(0xFF041E26)
                            )
                        )
                    }
                }
            }
        }

        // Horizontal Blueprint Carousel
        item {
            val filteredCategories = RobotCategory.values().filter {
                selectedGroupFilter == null || it.group == selectedGroupFilter
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredCategories) { category ->
                    BlueprintCard(
                        category = category,
                        userCapital = gameState.companyCapital,
                        canBuild = gameState.buildingRobots.size < gameState.factorySlotsTotal &&
                                gameState.companyCapital >= category.baseCost,
                        onSelect = {
                            selectedCategoryForBuild = category
                            customRobotName = ""
                            showBlueprintDialog = true
                        }
                    )
                }
            }
        }

        // Active Assembly Lines Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Fabrication Bays (${gameState.buildingRobots.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (gameState.buildingRobots.size >= gameState.factorySlotsTotal) {
                    Text(
                        text = "MAX CAPACITY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarningOrange
                    )
                }
            }
        }

        if (gameState.buildingRobots.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.PrecisionManufacturing,
                            contentDescription = "No active assembly",
                            tint = TextMuted,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Fabrication Lines are Idle",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Text(
                            text = "Select a product above (Robot, Phone, Car, Bike, TV, Drone) to fabricate.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(gameState.buildingRobots) { robot ->
                AssemblyLineCard(robot = robot)
            }
        }

        // Factory Inventory Section (Completed Units Ready for QA or Delivery)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Finished Warehouse Stock (${gameState.readyForDeliveryInventory.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Ready for QA & Customer Orders",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        if (gameState.readyForDeliveryInventory.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No products in warehouse. Fabricate new units above!",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            items(gameState.readyForDeliveryInventory) { robot ->
                InventoryRobotCard(
                    robot = robot,
                    onTestClick = { onNavigateToTesting(robot) },
                    onDeliverClick = { onNavigateToDelivery() }
                )
            }
        }
    }

    // Blueprint Confirmation Dialog
    if (showBlueprintDialog) {
        AlertDialog(
            onDismissRequest = { showBlueprintDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getIconForCategory(selectedCategoryForBuild),
                        contentDescription = selectedCategoryForBuild.title,
                        tint = CyberGold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Fabricate ${selectedCategoryForBuild.title}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = selectedCategoryForBuild.description,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Divider(color = DarkSurfaceBorder)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Production Cost:", fontSize = 12.sp, color = TextMuted)
                        Text(
                            text = "$${String.format("%,d", selectedCategoryForBuild.baseCost)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGoldBright
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Expected Market Value:", fontSize = 12.sp, color = TextMuted)
                        Text(
                            text = "$${String.format("%,d", selectedCategoryForBuild.basePrice)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberNeonGreen
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Fabrication Time:", fontSize = 12.sp, color = TextMuted)
                        Text(
                            text = "${selectedCategoryForBuild.buildTimeSec} seconds",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CyberCyan
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "City Transformation:", fontSize = 12.sp, color = TextMuted)
                        Text(
                            text = selectedCategoryForBuild.cityImpactType,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }

                    OutlinedTextField(
                        value = customRobotName,
                        onValueChange = { customRobotName = it },
                        label = { Text("Custom Product Name (Optional)") },
                        placeholder = { Text(selectedCategoryForBuild.title) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_name_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onStartBuild(selectedCategoryForBuild, customRobotName)
                        showBlueprintDialog = false
                    },
                    modifier = Modifier.testTag("confirm_build_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Authorize Build ($${String.format("%,d", selectedCategoryForBuild.baseCost)})",
                        color = Color(0xFF1A1000),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlueprintDialog = false }) {
                    Text(text = "Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun BlueprintCard(
    category: RobotCategory,
    userCapital: Long,
    canBuild: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAffordable = userCapital >= category.baseCost
    Surface(
        modifier = modifier
            .width(175.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = canBuild) { onSelect() }
            .testTag("blueprint_card_${category.name}"),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (canBuild) CyberCyan.copy(alpha = 0.5f) else DarkSurfaceBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getIconForCategory(category),
                        contentDescription = category.title,
                        tint = CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CyberGoldDark.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = "${category.buildTimeSec}s",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGoldBright,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = category.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )

            Text(
                text = category.cityImpactType,
                fontSize = 10.sp,
                color = TextMuted,
                maxLines = 1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${String.format("%,d", category.baseCost)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAffordable) CyberGoldBright else DangerRed
                )
                Text(
                    text = "Sell $${String.format("%,d", category.basePrice)}",
                    fontSize = 10.sp,
                    color = CyberNeonGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AssemblyLineCard(
    robot: RobotUnit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("assembly_line_${robot.id}"),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getIconForCategory(robot.category),
                        contentDescription = robot.customName,
                        tint = CyberGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = robot.customName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "SN: ${robot.serialNumber} • ${robot.category.cityImpactType}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CyberGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "FABRICATING ${(robot.buildProgress * 100).toInt()}%",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGoldBright,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Animated Linear Progress Bar
            LinearProgressIndicator(
                progress = { robot.buildProgress },
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

@Composable
fun InventoryRobotCard(
    robot: RobotUnit,
    onTestClick: () -> Unit,
    onDeliverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("inventory_robot_${robot.id}"),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (robot.isTested) SuccessGreen.copy(alpha = 0.5f) else DarkSurfaceBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getIconForCategory(robot.category),
                            contentDescription = robot.customName,
                            tint = CyberCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = robot.customName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${robot.category.title} • Value: $${String.format("%,d", robot.finalSalePrice)}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                // QA Grade Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(robot.testGrade.badgeColorHex).copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(robot.testGrade.badgeColorHex))
                ) {
                    Text(
                        text = robot.testGrade.gradeLetter,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(robot.testGrade.badgeColorHex),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onTestClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("test_robot_button_${robot.id}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = "QA Test",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (robot.isTested) "Re-Test QA" else "Run QA Test",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onDeliverClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("deliver_robot_button_${robot.id}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGold)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Deliver to Home",
                        tint = Color(0xFF1A1000),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Deliver",
                        fontSize = 11.sp,
                        color = Color(0xFF1A1000),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun getIconForCategory(category: RobotCategory): ImageVector {
    return when (category) {
        RobotCategory.CHEF_DOMESTIC -> Icons.Default.Restaurant
        RobotCategory.TITAN_HUMANOID -> Icons.Default.SmartToy
        RobotCategory.MEDICAL_CARE -> Icons.Default.HealthAndSafety
        RobotCategory.CONSTRUCTION_UTILITY -> Icons.Default.Engineering
        RobotCategory.AI_SMARTPHONE -> Icons.Default.Smartphone
        RobotCategory.AI_FOLDABLE_PHONE -> Icons.Default.DevicesFold
        RobotCategory.AI_AUTO_CAR -> Icons.Default.DirectionsCar
        RobotCategory.AI_CYBER_TRUCK -> Icons.Default.LocalShipping
        RobotCategory.AI_SMART_BIKE -> Icons.Default.TwoWheeler
        RobotCategory.AI_SMART_TV -> Icons.Default.Tv
        RobotCategory.DELIVERY_DRONE -> Icons.Default.Flight
        RobotCategory.FALCON_ORBITAL_DRONE -> Icons.Default.RocketLaunch
    }
}
