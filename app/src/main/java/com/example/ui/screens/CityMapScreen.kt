package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CityDistrict
import com.example.data.repository.GameState
import com.example.ui.theme.*

@Composable
fun CityMapScreen(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CitySimulation")
    val carAnimProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CarMoving"
    )
    val droneAnimProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DroneFlying"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("city_map_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Hero Smart City Image Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_smart_city),
                        contentDescription = "Nova Solis Metropolis",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, DarkBackground.copy(alpha = 0.85f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CyberGold.copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = "METROPOLIS LIVE RADAR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1A1000),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Nova Solis AI Metropolis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "City Transformation: ${gameState.overallCityAiTransformation.toInt()}% • Traffic Congestion Reduced by ${(gameState.overallCityAiTransformation * 0.4f).toInt()}%",
                            fontSize = 11.sp,
                            color = CyberCyan
                        )
                    }
                }
            }
        }

        // Live Radar Map Canvas
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(16.dp),
                color = DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.3f))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Grid Lines
                        val gridCols = 8
                        val gridRows = 5
                        for (i in 0..gridCols) {
                            val x = (w / gridCols) * i
                            drawLine(
                                color = Color(0x1500F5D4),
                                start = Offset(x, 0f),
                                end = Offset(x, h),
                                strokeWidth = 1f
                            )
                        }
                        for (j in 0..gridRows) {
                            val y = (h / gridRows) * j
                            drawLine(
                                color = Color(0x1500F5D4),
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1f
                            )
                        }

                        // Roads Highway Loop
                        drawLine(
                            color = Color(0x44FFB703),
                            start = Offset(40f, h * 0.5f),
                            end = Offset(w - 40f, h * 0.5f),
                            strokeWidth = 6f
                        )
                        drawLine(
                            color = Color(0x44FFB703),
                            start = Offset(w * 0.5f, 30f),
                            end = Offset(w * 0.5f, h - 30f),
                            strokeWidth = 6f
                        )

                        // Moving AI Cars along Highway
                        val carX = 40f + (w - 80f) * carAnimProgress
                        drawCircle(
                            color = CyberCyan,
                            radius = 6.dp.toPx(),
                            center = Offset(carX, h * 0.5f)
                        )
                        val carY = 30f + (h - 60f) * ((carAnimProgress + 0.5f) % 1f)
                        drawCircle(
                            color = CyberGold,
                            radius = 6.dp.toPx(),
                            center = Offset(w * 0.5f, carY)
                        )

                        // Moving Sky Delivery Drone
                        val droneX = 60f + (w - 120f) * droneAnimProgress
                        val droneY = 40f + (h * 0.3f) * (1f - droneAnimProgress)
                        drawCircle(
                            color = CyberPink,
                            radius = 7.dp.toPx(),
                            center = Offset(droneX, droneY)
                        )

                        // District Nodes
                        val districts = gameState.cityDistricts
                        val positions = listOf(
                            Offset(w * 0.25f, h * 0.28f),
                            Offset(w * 0.75f, h * 0.28f),
                            Offset(w * 0.25f, h * 0.72f),
                            Offset(w * 0.75f, h * 0.72f)
                        )

                        districts.take(4).forEachIndexed { idx, dist ->
                            val pos = positions[idx]
                            drawCircle(
                                color = Color(dist.accentColorHex).copy(alpha = 0.25f),
                                radius = 22.dp.toPx(),
                                center = pos
                            )
                            drawCircle(
                                color = Color(dist.accentColorHex),
                                radius = 8.dp.toPx(),
                                center = pos
                            )
                        }
                    }

                    // Legend
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkBackground.copy(alpha = 0.85f))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendItem(color = CyberCyan, label = "AI Cars & Trucks")
                        LegendItem(color = CyberPink, label = "Sky Drones")
                        LegendItem(color = CyberGold, label = "Smart Bikes")
                    }
                }
            }
        }

        // District Breakdown Cards
        item {
            Text(
                text = "Metropolis AI District Deployments",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        items(gameState.cityDistricts) { district ->
            DistrictCard(district = district)
        }
    }
}

@Composable
fun DistrictCard(
    district: CityDistrict,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("district_card_${district.id}"),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(district.accentColorHex).copy(alpha = 0.3f)
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
                Column {
                    Text(
                        text = district.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = district.tagLine,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(district.accentColorHex).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${district.aiTransformationPercent.toInt()}% AI",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(district.accentColorHex),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Fleet Counts in District (Cars, Bikes, Drones, Phones, TVs, Domestic, Utility)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FleetBadge(label = "Cars", count = district.aiCarsCount, icon = Icons.Default.DirectionsCar)
                FleetBadge(label = "Bikes", count = district.aiBikesCount, icon = Icons.Default.TwoWheeler)
                FleetBadge(label = "Drones", count = district.deliveryDronesCount, icon = Icons.Default.Flight)
                FleetBadge(label = "Phones", count = district.smartphonesCount, icon = Icons.Default.Smartphone)
                FleetBadge(label = "TVs", count = district.smartTvsCount, icon = Icons.Default.Tv)
                FleetBadge(label = "Robots", count = district.domesticBotsCount + district.utilityBotsCount + district.humanoidsCount, icon = Icons.Default.SmartToy)
            }
        }
    }
}

@Composable
fun FleetBadge(
    label: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = DarkSurfaceHighlight
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = TextSecondary,
                modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "$count",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 10.sp, color = TextSecondary)
    }
}
