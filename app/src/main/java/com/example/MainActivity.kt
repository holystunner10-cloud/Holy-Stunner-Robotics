package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.DailyReport
import com.example.data.model.RobotUnit
import com.example.ui.components.DailySummaryDialog
import com.example.ui.components.NewsTickerBar
import com.example.ui.components.TopBarStats
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.GameViewModel

enum class MainTab(val title: String, val icon: ImageVector, val tag: String) {
    FACTORY("Factory", Icons.Default.PrecisionManufacturing, "tab_factory"),
    EMPLOYEES("CEO & Staff", Icons.Default.Groups, "tab_employees"),
    DELIVERY("Orders", Icons.Default.LocalShipping, "tab_delivery"),
    CITY_MAP("City Map", Icons.Default.LocationCity, "tab_city_map"),
    TESTING("QA Lab", Icons.Default.Science, "tab_testing"),
    AI_BRAIN("AI Brain", Icons.Default.Psychology, "tab_ai_brain"),
    RESEARCH("R&D Tech", Icons.Default.Bolt, "tab_research"),
    ACHIEVEMENTS("Trophies", Icons.Default.EmojiEvents, "tab_achievements")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                HolyStunnerApp()
            }
        }
    }
}

@Composable
fun HolyStunnerApp(
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    var currentTab by remember { mutableStateOf(MainTab.FACTORY) }
    var selectedRobotForTesting by remember { mutableStateOf<RobotUnit?>(null) }
    var activeDailyReport by remember { mutableStateOf<DailyReport?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                TopBarStats(
                    capital = gameState.companyCapital,
                    researchPoints = gameState.researchPoints,
                    cityAiPercent = gameState.overallCityAiTransformation,
                    brandTier = gameState.companyTier,
                    reputation = gameState.brandReputation,
                    cityHype = gameState.cityHype,
                    currentDay = gameState.currentDay,
                    dayPhase = gameState.dayPhase,
                    simulationSpeed = gameState.simulationSpeed,
                    onAdvanceDay = {
                        activeDailyReport = viewModel.advanceDay()
                    },
                    onSetSpeed = { speed ->
                        viewModel.setSimulationSpeed(speed)
                    }
                )
                NewsTickerBar(
                    newsFeed = gameState.newsFeed,
                    activeEvent = gameState.activeEmergentEvent
                )
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav"),
                color = DarkSurfaceElevated,
                tonalElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) CyberGold.copy(alpha = 0.15f) else Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) CyberGold else Color.Transparent
                            ),
                            onClick = { currentTab = tab },
                            modifier = Modifier.testTag(tab.tag)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) CyberGold else TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = tab.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) CyberGoldBright else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            when (currentTab) {
                MainTab.FACTORY -> {
                    FactoryScreen(
                        gameState = gameState,
                        onStartBuild = { category, name ->
                            viewModel.startBuildingRobot(category, name)
                        },
                        onNavigateToTesting = { robot ->
                            selectedRobotForTesting = robot
                            currentTab = MainTab.TESTING
                        },
                        onNavigateToDelivery = {
                            currentTab = MainTab.DELIVERY
                        }
                    )
                }

                MainTab.EMPLOYEES -> {
                    EmployeeManagementScreen(
                        gameState = gameState,
                        onHireEmployee = { candId ->
                            viewModel.hireEmployee(candId)
                        },
                        onFireEmployee = { empId ->
                            viewModel.fireEmployee(empId)
                        },
                        onTrainEmployee = { empId ->
                            viewModel.trainEmployee(empId)
                        },
                        onUpgradeHq = {
                            viewModel.upgradeHq()
                        },
                        onExecuteCeoAction = { action ->
                            viewModel.executeCeoAction(action)
                        }
                    )
                }

                MainTab.DELIVERY -> {
                    MarketDeliveryScreen(
                        gameState = gameState,
                        onDeliverOrder = { orderId, robotId ->
                            viewModel.deliverRobotToCustomer(orderId, robotId)
                        }
                    )
                }

                MainTab.CITY_MAP -> {
                    CityMapScreen(gameState = gameState)
                }

                MainTab.TESTING -> {
                    TestingLabScreen(
                        gameState = gameState,
                        selectedRobot = selectedRobotForTesting,
                        onSelectRobot = { robot ->
                            selectedRobotForTesting = robot
                        },
                        onCompleteTesting = { robotId, score, grade ->
                            viewModel.completeTesting(robotId, score, grade)
                        }
                    )
                }

                MainTab.AI_BRAIN -> {
                    AiBrainLabScreen(
                        gameState = gameState,
                        onTrainPerk = { robotId, perk, rp ->
                            viewModel.trainAiPerk(robotId, perk, rp)
                        },
                        onUpdateBrainConfig = { robotId, config ->
                            viewModel.updateAiBrain(robotId, config)
                        }
                    )
                }

                MainTab.RESEARCH -> {
                    ResearchScreen(
                        gameState = gameState,
                        onUnlockTech = { techId ->
                            viewModel.unlockTechNode(techId)
                        }
                    )
                }

                MainTab.ACHIEVEMENTS -> {
                    AchievementsScreen(
                        gameState = gameState,
                        onClaimReward = { achId ->
                            viewModel.claimAchievementReward(achId)
                        }
                    )
                }
            }
        }
    }

    // Daily Summary Report Dialog
    activeDailyReport?.let { report ->
        DailySummaryDialog(
            report = report,
            onDismiss = { activeDailyReport = null }
        )
    }
}
