package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RobotCategory
import com.example.data.model.RobotUnit
import com.example.data.model.TestGrade
import com.example.data.repository.GameState
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun TestingLabScreen(
    gameState: GameState,
    selectedRobot: RobotUnit?,
    onSelectRobot: (RobotUnit) -> Unit,
    onCompleteTesting: (String, Int, TestGrade) -> Unit,
    modifier: Modifier = Modifier
) {
    val availableRobots = gameState.readyForDeliveryInventory

    var currentRobot by remember(selectedRobot, availableRobots) {
        mutableStateOf(selectedRobot ?: availableRobots.firstOrNull())
    }

    var isTestInProgress by remember { mutableStateOf(false) }
    var testCompleted by remember { mutableStateOf(false) }
    var testScore by remember { mutableStateOf(0) }
    var calculatedGrade by remember { mutableStateOf(TestGrade.UNTESTED) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("testing_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DarkSurfaceElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = "QA Test Lab",
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Holy Stunner QA Testing Labs",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Rigorous safety, AI cognition & stress tests before consumer dispatch",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Unit Selector Carousel
        if (availableRobots.isNotEmpty()) {
            Text(
                text = "Select Product to Test in Chamber",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(availableRobots) { bot ->
                    val isSelected = currentRobot?.id == bot.id
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) CyberCyan.copy(alpha = 0.15f) else DarkSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) CyberCyan else DarkSurfaceBorder
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                currentRobot = bot
                                isTestInProgress = false
                                testCompleted = false
                            }
                            .testTag("select_test_unit_${bot.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = getIconForCategory(bot.category),
                                contentDescription = bot.customName,
                                tint = if (isSelected) CyberCyan else TextSecondary,
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
                                    text = "Grade: ${bot.testGrade.gradeLetter}",
                                    fontSize = 10.sp,
                                    color = Color(bot.testGrade.badgeColorHex)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Testing Simulator Area
        val targetRobot = currentRobot
        if (targetRobot == null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "No units available",
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No products ready for testing",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Text(
                            text = "Build products in the Factory first, then return here to test!",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.3f))
            ) {
                if (testCompleted) {
                    // Test Result Certification
                    TestCertificationResultView(
                        robot = targetRobot,
                        score = testScore,
                        grade = calculatedGrade,
                        onCertify = {
                            onCompleteTesting(targetRobot.id, testScore, calculatedGrade)
                            testCompleted = false
                            isTestInProgress = false
                        },
                        onRetest = {
                            testCompleted = false
                            isTestInProgress = true
                        }
                    )
                } else if (isTestInProgress) {
                    // Active Simulator based on category
                    when (targetRobot.category) {
                        RobotCategory.CHEF_DOMESTIC -> {
                            ChefDexteritySimulator(
                                onFinishTest = { score, grade ->
                                    testScore = score
                                    calculatedGrade = grade
                                    testCompleted = true
                                    isTestInProgress = false
                                }
                            )
                        }
                        RobotCategory.AI_AUTO_CAR, RobotCategory.AI_CYBER_TRUCK, RobotCategory.AI_SMART_BIKE -> {
                            AutonomousDrivingSimulator(
                                isBike = targetRobot.category == RobotCategory.AI_SMART_BIKE,
                                onFinishTest = { score, grade ->
                                    testScore = score
                                    calculatedGrade = grade
                                    testCompleted = true
                                    isTestInProgress = false
                                }
                            )
                        }
                        RobotCategory.DELIVERY_DRONE, RobotCategory.FALCON_ORBITAL_DRONE -> {
                            DroneLandingSimulator(
                                onFinishTest = { score, grade ->
                                    testScore = score
                                    calculatedGrade = grade
                                    testCompleted = true
                                    isTestInProgress = false
                                }
                            )
                        }
                        RobotCategory.MEDICAL_CARE,
                        RobotCategory.CONSTRUCTION_UTILITY,
                        RobotCategory.TITAN_HUMANOID,
                        RobotCategory.AI_SMARTPHONE,
                        RobotCategory.AI_FOLDABLE_PHONE,
                        RobotCategory.AI_SMART_TV -> {
                            TuringEmpathyDiagnosticsSimulator(
                                robot = targetRobot,
                                onFinishTest = { score, grade ->
                                    testScore = score
                                    calculatedGrade = grade
                                    testCompleted = true
                                    isTestInProgress = false
                                }
                            )
                        }
                    }
                } else {
                    // Pre-Test Briefing Screen
                    PreTestBriefingView(
                        robot = targetRobot,
                        onStartTest = {
                            isTestInProgress = true
                            testCompleted = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PreTestBriefingView(
    robot: RobotUnit,
    onStartTest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(CyberGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconForCategory(robot.category),
                    contentDescription = robot.customName,
                    tint = CyberGold,
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = "Simulation Chamber: ${robot.category.title}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = when (robot.category) {
                    RobotCategory.CHEF_DOMESTIC -> "Test culinary multi-arm spatula timing, heat regulation & zero-spill grip under restaurant rush conditions."
                    RobotCategory.TITAN_HUMANOID -> "Test bipedal balance, high-torque heavy lifting, and collaborative human assistance protocols."
                    RobotCategory.AI_AUTO_CAR -> "Test level-5 collision avoidance, sudden pedestrian detection, and rainstorm traction algorithms."
                    RobotCategory.AI_CYBER_TRUCK -> "Test armored payload hauling, off-road terrain adaptability, and crash barrier resilience."
                    RobotCategory.AI_SMART_BIKE -> "Test gyroscopic auto-balance, alleyway navigation, and smart commuter safety barriers."
                    RobotCategory.AI_SMARTPHONE -> "Test local neural NPU latency, quantum encryption, and holographic AI agent responses."
                    RobotCategory.AI_FOLDABLE_PHONE -> "Test dual-screen multitasking, flexible OLED hinge durability, and multi-modal vision AI."
                    RobotCategory.AI_SMART_TV -> "Test 8K neural upscaling, ambient room presence radar, and hands-free smart home hub orchestration."
                    RobotCategory.DELIVERY_DRONE -> "Test crosswind altitude stabilization, laser radar obstacle evasion, and precision doorstep landing."
                    RobotCategory.FALCON_ORBITAL_DRONE -> "Test hypersonic atmospheric entry, ion engine vectoring, and orbital satellite deployment."
                    RobotCategory.MEDICAL_CARE -> "Test empathetic dialogue engine, vitals telemetry accuracy, and emergency triage response."
                    RobotCategory.CONSTRUCTION_UTILITY -> "Test hydraulic torque limits, solar panel micro-alignment, and structural safety protocols."
                },
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Divider(color = DarkSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Current Status", fontSize = 11.sp, color = TextMuted)
                    Text(
                        text = robot.testGrade.gradeLetter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(robot.testGrade.badgeColorHex)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Firmware OS", fontSize = 11.sp, color = TextMuted)
                    Text(
                        text = robot.brainConfig.firmwareVersion,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "AI IQ Rating", fontSize = 11.sp, color = TextMuted)
                    Text(
                        text = "${robot.brainConfig.overallIntelligenceRating} IQ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGoldBright
                    )
                }
            }
        }

        Button(
            onClick = onStartTest,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("launch_simulation_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Start Simulation",
                tint = Color(0xFF00201C)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Launch QA Simulator",
                color = Color(0xFF00201C),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// 1. Interactive Chef Dexterity Simulator (Pancake / Sauté Flip timing)
@Composable
fun ChefDexteritySimulator(
    onFinishTest: (Int, TestGrade) -> Unit
) {
    var round by remember { mutableStateOf(1) }
    var totalScore by remember { mutableStateOf(0) }
    var lastFeedback by remember { mutableStateOf("Tap 'FLIP NOW' when the needle hits the Golden Sweet Spot!") }
    var needleProgress by remember { mutableStateOf(0f) }
    var isMovingForward by remember { mutableStateOf(true) }

    LaunchedEffect(round) {
        while (round <= 3) {
            delay(16)
            if (isMovingForward) {
                needleProgress += 0.035f
                if (needleProgress >= 1f) isMovingForward = false
            } else {
                needleProgress -= 0.035f
                if (needleProgress <= 0f) isMovingForward = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🍳 Culinary Spatula Dexterity Test",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CyberGoldBright
            )
            Text(
                text = "Round $round of 3 • Total Score: $totalScore pts",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        // Visual Heat / Flip Timing Gauge
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                DangerRed,
                                WarningOrange,
                                SuccessGreen,
                                CyberGold,
                                SuccessGreen,
                                WarningOrange,
                                DangerRed
                            )
                        )
                    )
                    .border(2.dp, DarkSurfaceBorder, RoundedCornerShape(18.dp))
            ) {
                // Moving needle indicator
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = (needleProgress * 280).dp)
                        .background(Color.White, RoundedCornerShape(3.dp))
                )
            }

            Text(
                text = lastFeedback,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyberCyan,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = {
                val diffFromCenter = kotlin.math.abs(needleProgress - 0.5f)
                val roundPoints = when {
                    diffFromCenter < 0.08f -> {
                        lastFeedback = "🌟 PERFECT MICHELIN FLIP! (+35 pts)"
                        35
                    }
                    diffFromCenter < 0.18f -> {
                        lastFeedback = "👍 Solid Gourmet Flip (+25 pts)"
                        25
                    }
                    diffFromCenter < 0.30f -> {
                        lastFeedback = "⚠️ Minor batter wobble (+15 pts)"
                        15
                    }
                    else -> {
                        lastFeedback = "🔥 Burnt / Missed flip! (+5 pts)"
                        5
                    }
                }
                totalScore += roundPoints
                if (round >= 3) {
                    val finalGrade = when {
                        totalScore >= 90 -> TestGrade.GRADE_S
                        totalScore >= 75 -> TestGrade.GRADE_A
                        totalScore >= 50 -> TestGrade.GRADE_B
                        else -> TestGrade.GRADE_C
                    }
                    onFinishTest(totalScore, finalGrade)
                } else {
                    round++
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("flip_action_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyberGold)
        ) {
            Text(
                text = "FLIP NOW (Round $round/3)",
                color = Color(0xFF1A1000),
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

// 2. Autonomous Car & Smart Bike Obstacle Course Simulator
@Composable
fun AutonomousDrivingSimulator(
    isBike: Boolean,
    onFinishTest: (Int, TestGrade) -> Unit
) {
    var playerLane by remember { mutableStateOf(1) } // 0: Left, 1: Center, 2: Right
    var obstacleLane by remember { mutableStateOf(1) }
    var obstacleY by remember { mutableStateOf(0f) }
    var wavesRemaining by remember { mutableStateOf(4) }
    var collisions by remember { mutableStateOf(0) }
    var successfulEvades by remember { mutableStateOf(0) }

    LaunchedEffect(wavesRemaining) {
        if (wavesRemaining > 0) {
            obstacleLane = Random.nextInt(3)
            obstacleY = 0f
            while (obstacleY < 1f) {
                delay(30)
                obstacleY += 0.045f
            }
            if (playerLane == obstacleLane) {
                collisions++
            } else {
                successfulEvades++
            }
            wavesRemaining--
        } else {
            val score = ((successfulEvades.toFloat() / 4f) * 100).toInt()
            val grade = when {
                score == 100 -> TestGrade.GRADE_S
                score >= 75 -> TestGrade.GRADE_A
                score >= 50 -> TestGrade.GRADE_B
                else -> TestGrade.GRADE_C
            }
            onFinishTest(score, grade)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isBike) "🚲 AI Smart Bike Collision Shield Test" else "🚗 AI HyperDrive Autonomous Course",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CyberCyan
        )

        // 3-Lane Road Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF070E1A))
                .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val laneWidth = size.width / 3
                // Lane dividers
                drawLine(
                    color = Color(0x3300F5D4),
                    start = Offset(laneWidth, 0f),
                    end = Offset(laneWidth, size.height),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color(0x3300F5D4),
                    start = Offset(laneWidth * 2, 0f),
                    end = Offset(laneWidth * 2, size.height),
                    strokeWidth = 2f
                )

                // Draw Approaching Obstacle
                val obsX = obstacleLane * laneWidth + (laneWidth / 2)
                val obsY = obstacleY * (size.height - 40.dp.toPx())
                drawCircle(
                    color = DangerRed,
                    radius = 16.dp.toPx(),
                    center = Offset(obsX, obsY)
                )

                // Draw Player Robot / Vehicle
                val playerX = playerLane * laneWidth + (laneWidth / 2)
                val playerY = size.height - 30.dp.toPx()
                drawCircle(
                    color = CyberGold,
                    radius = 18.dp.toPx(),
                    center = Offset(playerX, playerY)
                )
            }
        }

        Text(
            text = "Approaching hazard: Switch lanes to evade! (Evaded: $successfulEvades / 4)",
            fontSize = 12.sp,
            color = TextSecondary
        )

        // Steer Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { if (playerLane > 0) playerLane-- },
                enabled = playerLane > 0,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("steer_left_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceHighlight)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Left", tint = CyberCyan)
                Text(text = "Left", color = TextPrimary)
            }
            Button(
                onClick = { if (playerLane < 2) playerLane++ },
                enabled = playerLane < 2,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("steer_right_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceHighlight)
            ) {
                Text(text = "Right", color = TextPrimary)
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Right", tint = CyberCyan)
            }
        }
    }
}

// 3. Drone Wind Tunnel & Precision Landing Simulator
@Composable
fun DroneLandingSimulator(
    onFinishTest: (Int, TestGrade) -> Unit
) {
    var droneX by remember { mutableStateOf(0.5f) }
    var droneAltitude by remember { mutableStateOf(1.0f) }
    var windForce by remember { mutableStateOf(0.015f) }

    LaunchedEffect(Unit) {
        while (droneAltitude > 0.05f) {
            delay(40)
            droneAltitude -= 0.02f
            droneX = (droneX + windForce).coerceIn(0.05f, 0.95f)
            if (Random.nextInt(10) > 7) {
                windForce = (Random.nextFloat() - 0.5f) * 0.035f
            }
        }
        val error = kotlin.math.abs(droneX - 0.5f)
        val score = ((1.0f - (error * 2)).coerceIn(0f, 1f) * 100).toInt()
        val grade = when {
            score >= 90 -> TestGrade.GRADE_S
            score >= 75 -> TestGrade.GRADE_A
            score >= 50 -> TestGrade.GRADE_B
            else -> TestGrade.GRADE_C
        }
        onFinishTest(score, grade)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🚁 AeroDrop Wind Tunnel & Doorstep Landing",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CyberCyan
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF060D1A))
                .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val targetCenter = Offset(size.width * 0.5f, size.height - 15.dp.toPx())
                drawCircle(
                    color = SuccessGreen.copy(alpha = 0.4f),
                    radius = 35.dp.toPx(),
                    center = targetCenter
                )
                drawCircle(
                    color = SuccessGreen,
                    radius = 12.dp.toPx(),
                    center = targetCenter
                )

                val dX = size.width * droneX
                val dY = (1.0f - droneAltitude) * (size.height - 40.dp.toPx())
                drawCircle(
                    color = CyberGold,
                    radius = 16.dp.toPx(),
                    center = Offset(dX, dY)
                )
            }
        }

        Text(
            text = "Altitude: ${(droneAltitude * 100).toInt()}m • Pulse thrusters to align with green target pad!",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { droneX = (droneX - 0.08f).coerceAtLeast(0.05f) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("thrust_left_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
            ) {
                Text(text = "◀ Pulse Left", color = Color(0xFF00201C), fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { droneX = (droneX + 0.08f).coerceAtMost(0.95f) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("thrust_right_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
            ) {
                Text(text = "Pulse Right ▶", color = Color(0xFF00201C), fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 4. Turing Empathy & Safety Diagnostics Simulator
@Composable
fun TuringEmpathyDiagnosticsSimulator(
    robot: RobotUnit,
    onFinishTest: (Int, TestGrade) -> Unit
) {
    var questionIndex by remember { mutableStateOf(0) }
    var accumulatedScore by remember { mutableStateOf(0) }

    val scenarios = listOf(
        Pair(
            "An elderly citizen asks: 'I feel a little dizzy, should I make some tea?'",
            listOf(
                Pair("Immediately check vitals telemetry, offer gentle seating, and prepare mild chamomile.", 35),
                Pair("Brew the tea at boiling temperature right away.", 15),
                Pair("Display an error code and wait for human reset.", 5)
            )
        ),
        Pair(
            "A pedestrian with groceries walks across the construction zone suddenly:",
            listOf(
                Pair("Instantly freeze actuators, activate acoustic warning & shield pedestrian.", 35),
                Pair("Continue moving solar beam slowly hoping they move away.", 10),
                Pair("Honk loud siren horn and accelerate past.", 5)
            )
        ),
        Pair(
            "Customer asks: 'Can you teach my grandchildren how your AI brain works?'",
            listOf(
                Pair("Engage polite educational dialogue mode with interactive friendly lights!", 30),
                Pair("Print raw machine code and binary assembly dump.", 10),
                Pair("State that proprietary code is confidential and go to sleep.", 5)
            )
        )
    )

    val currentScenario = scenarios.getOrNull(questionIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "🧠 Turing Empathy & Neural Diagnostics",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CyberGoldBright
            )
            Text(
                text = "Scenario ${questionIndex + 1} of 3",
                fontSize = 12.sp,
                color = TextMuted
            )

            if (currentScenario != null) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurfaceHighlight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = currentScenario.first,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Text(
                    text = "Select optimal AI neural response:",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                currentScenario.second.forEachIndexed { idx, opt ->
                    OutlinedButton(
                        onClick = {
                            accumulatedScore += opt.second
                            if (questionIndex >= 2) {
                                val finalGrade = when {
                                    accumulatedScore >= 85 -> TestGrade.GRADE_S
                                    accumulatedScore >= 65 -> TestGrade.GRADE_A
                                    accumulatedScore >= 45 -> TestGrade.GRADE_B
                                    else -> TestGrade.GRADE_C
                                }
                                onFinishTest(accumulatedScore, finalGrade)
                            } else {
                                questionIndex++
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("empathy_option_${questionIndex}_$idx"),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = opt.first,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TestCertificationResultView(
    robot: RobotUnit,
    score: Int,
    grade: TestGrade,
    onCertify: () -> Unit,
    onRetest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(grade.badgeColorHex).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Certified",
                    tint = Color(grade.badgeColorHex),
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = "QA Certification Complete!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(grade.badgeColorHex).copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(grade.badgeColorHex))
            ) {
                Text(
                    text = "CERTIFIED: ${grade.gradeLetter}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(grade.badgeColorHex),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Text(
                text = "Simulator Score: $score / 100 • Market Payout Multiplier: ${(grade.multiplier * 100).toInt()}%",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Divider(color = DarkSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = DarkSurfaceHighlight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Final Value", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "$${String.format("%,d", (robot.category.basePrice * grade.multiplier).toLong())}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGoldBright
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Research Earned", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "+25 RP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberCyan
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onRetest,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("retest_button"),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TextMuted)
            ) {
                Text(text = "Re-Test", color = TextSecondary)
            }
            Button(
                onClick = onCertify,
                modifier = Modifier
                    .weight(1.5f)
                    .height(48.dp)
                    .testTag("certify_and_save_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyberGold)
            ) {
                Text(
                    text = "Certify & Save",
                    color = Color(0xFF1A1000),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
