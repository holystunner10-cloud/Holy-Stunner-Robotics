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
import com.example.data.model.Department
import com.example.data.model.Employee
import com.example.data.model.HqTier
import com.example.data.repository.CeoActionType
import com.example.data.repository.GameState
import com.example.ui.theme.*

@Composable
fun EmployeeManagementScreen(
    gameState: GameState,
    onHireEmployee: (String) -> Unit,
    onFireEmployee: (String) -> Unit,
    onTrainEmployee: (String) -> Unit,
    onUpgradeHq: () -> Unit,
    onExecuteCeoAction: (CeoActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Staff Roster, 1 = Talent Market, 2 = CEO Actions & HQ

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("employee_management_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // CEO Hero Profile Card (Elon / Steve Jobs style founder overview)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .testTag("ceo_profile_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CyberGold.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(CyberGold, CyberAmber)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = gameState.ceoProfile.avatarEmoji,
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = gameState.ceoProfile.founderName,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black,
                                        color = CyberGoldBright
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = CyberCyan.copy(alpha = 0.2f),
                                        border = androidx.compose.foundation.BorderStroke(0.5.dp, CyberCyan)
                                    ) {
                                        Text(
                                            text = "FOUNDER & CEO",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyberCyan,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = gameState.ceoProfile.title,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CEO Stats Matrix
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CeoStatChip(
                            label = "Engineering",
                            value = "${gameState.ceoProfile.engineeringSkill}/100",
                            icon = Icons.Default.Engineering,
                            color = CyberCyan,
                            modifier = Modifier.weight(1f)
                        )
                        CeoStatChip(
                            label = "Vision Level",
                            value = "Tier ${gameState.ceoProfile.visionLevel}",
                            icon = Icons.Default.Visibility,
                            color = CyberGold,
                            modifier = Modifier.weight(1f)
                        )
                        CeoStatChip(
                            label = "Team Size",
                            value = "${gameState.employees.size}/${gameState.ceoProfile.currentHqTier.capacity}",
                            icon = Icons.Default.Groups,
                            color = CyberPink,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (gameState.isSoloFounder) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberPink.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberPink.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "Solo Bonus",
                                    tint = CyberPink,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Working Solo in Garage! Founder crafts at +35% solo speed.",
                                    fontSize = 11.sp,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Sub-tabs: Staff Roster, Talent Recruitment, CEO Actions & HQ
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurfaceElevated,
                contentColor = CyberGold,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Staff (${gameState.employees.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Recruit (${gameState.availableCandidates.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("HQ & CEO Actions", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Tab 0: Current Employees Roster
                if (gameState.employees.isEmpty()) {
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
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Solo Founder",
                                    tint = CyberGold,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "You are currently working alone as Solo Founder!",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Ready to scale into a robotics empire? Switch to 'Recruit' tab to hire AI researchers, hardware engineers and PR marketers!",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(gameState.employees) { emp ->
                        EmployeeRosterCard(
                            employee = emp,
                            userCapital = gameState.companyCapital,
                            onTrain = { onTrainEmployee(emp.id) },
                            onFire = { onFireEmployee(emp.id) }
                        )
                    }
                }
            }

            1 -> {
                // Tab 1: Talent Recruitment Market
                item {
                    Text(
                        text = "Metropolis Talent Pool (Hire Top Engineers & Researchers)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                if (gameState.availableCandidates.isEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurfaceElevated
                        ) {
                            Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                                Text("No new candidates today. Advance the day to refresh the talent market!", fontSize = 12.sp, color = TextMuted)
                            }
                        }
                    }
                } else {
                    items(gameState.availableCandidates) { cand ->
                        val canHire = gameState.employees.size < gameState.ceoProfile.currentHqTier.capacity &&
                                gameState.companyCapital >= (cand.dailySalary * 2)

                        CandidateCard(
                            candidate = cand,
                            canHire = canHire,
                            onHire = { onHireEmployee(cand.id) }
                        )
                    }
                }
            }

            2 -> {
                // Tab 2: Headquarters Upgrade & CEO Executive Keynote Actions
                item {
                    HqUpgradeCard(
                        currentTier = gameState.ceoProfile.currentHqTier,
                        userCapital = gameState.companyCapital,
                        onUpgrade = onUpgradeHq
                    )
                }

                item {
                    Text(
                        text = "Executive Keynote & PR Actions (Elon / GTA Style)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                items(CeoActionType.values()) { action ->
                    CeoActionCard(
                        action = action,
                        canAfford = gameState.companyCapital >= action.cost,
                        onExecute = { onExecuteCeoAction(action) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CeoStatChip(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = DarkBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(text = label, fontSize = 9.sp, color = TextMuted)
                Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        }
    }
}

@Composable
private fun EmployeeRosterCard(
    employee: Employee,
    userCapital: Long,
    onTrain: () -> Unit,
    onFire: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("employee_card_${employee.id}"),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
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
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = employee.avatarEmoji, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = employee.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${employee.roleTitle} • Lv.${employee.level}",
                            fontSize = 11.sp,
                            color = CyberGoldBright
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CyberNeonGreen.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "+${employee.productivityBoostPercent}% Boost",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNeonGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = employee.traitDescription,
                fontSize = 11.sp,
                color = TextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Salary: $${String.format("%,d", employee.dailySalary)}",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = onTrain,
                        enabled = userCapital >= employee.upgradeCost,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Color(0xFF041E26)),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Upgrade, contentDescription = "Train", modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Train ($${String.format("%,d", employee.upgradeCost)})", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onFire,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DangerRed.copy(alpha = 0.6f)),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(text = "Release", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CandidateCard(
    candidate: Employee,
    canHire: Boolean,
    onHire: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("candidate_card_${candidate.id}"),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
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
                    Text(text = candidate.avatarEmoji, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = candidate.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${candidate.roleTitle} • ${candidate.department.displayName}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }

                Text(
                    text = "Skill ${candidate.skillRating}/100",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGoldBright
                )
            }

            Text(
                text = candidate.traitDescription,
                fontSize = 11.sp,
                color = TextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Payroll: $${String.format("%,d", candidate.dailySalary)}/day",
                    fontSize = 11.sp,
                    color = CyberNeonGreen
                )

                Button(
                    onClick = onHire,
                    enabled = canHire,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color(0xFF1A1000)),
                    modifier = Modifier
                        .height(30.dp)
                        .testTag("hire_button_${candidate.id}")
                ) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Hire", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Hire Specialist", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun HqUpgradeCard(
    currentTier: HqTier,
    userCapital: Long,
    onUpgrade: () -> Unit
) {
    val nextTier = when (currentTier) {
        HqTier.SUBURBAN_GARAGE -> HqTier.INNOVATION_WAREHOUSE
        HqTier.INNOVATION_WAREHOUSE -> HqTier.SILICON_VALLEY_CAMPUS
        HqTier.SILICON_VALLEY_CAMPUS -> HqTier.GIGAFACTORY_STARBASE
        HqTier.GIGAFACTORY_STARBASE -> null
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hq_upgrade_card"),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
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
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = "HQ",
                        tint = CyberGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Current HQ: ${currentTier.title}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGoldBright
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CyberCyan.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Max ${currentTier.capacity} Staff",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = currentTier.description,
                fontSize = 11.sp,
                color = TextSecondary
            )

            if (nextTier != null) {
                Divider(color = DarkSurfaceBorder)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Next: ${nextTier.title}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Capacity: ${nextTier.capacity} Staff • Cost: $${String.format("%,d", nextTier.upgradeCost)}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = onUpgrade,
                        enabled = userCapital >= nextTier.upgradeCost,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color(0xFF1A1000)),
                        modifier = Modifier.testTag("upgrade_hq_button")
                    ) {
                        Text(text = "Upgrade HQ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Text(
                    text = "🌟 Max Headquarters Tier Reached (Gigafactory Starbase)!",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGoldBright
                )
            }
        }
    }
}

@Composable
private fun CeoActionCard(
    action: CeoActionType,
    canAfford: Boolean,
    onExecute: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ceo_action_${action.name}"),
        shape = RoundedCornerShape(10.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = action.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = action.description,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "+${action.rpBonus} RP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan
                    )
                    Text(
                        text = "+${action.hypeBonus}% Hype",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPink
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = onExecute,
                enabled = canAfford,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Color(0xFF041E26)),
                modifier = Modifier.testTag("execute_action_${action.name}")
            ) {
                Text(
                    text = "$${String.format("%,d", action.cost)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
