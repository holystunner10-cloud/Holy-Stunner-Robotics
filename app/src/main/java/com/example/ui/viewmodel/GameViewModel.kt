package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.CeoActionType
import com.example.data.repository.GameRepository
import com.example.data.repository.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: GameRepository = GameRepository()
) : ViewModel() {

    val gameState: StateFlow<GameState> = repository.gameState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.gameState.value
    )

    init {
        startSimulationLoop()
    }

    private fun startSimulationLoop() {
        viewModelScope.launch {
            var tickCount = 0
            while (true) {
                val speed = gameState.value.simulationSpeed
                val delayMs = if (speed > 0) (1000L / speed) else 1000L
                delay(delayMs)

                if (speed > 0) {
                    tickCount++

                    val current = gameState.value

                    // 1. Advance robot/product builds
                    current.buildingRobots.forEach { bot ->
                        val totalSec = bot.category.buildTimeSec.toFloat()
                        val increment = 1.0f / totalSec
                        repository.advanceRobotBuildProgress(bot.id, increment)
                    }

                    // 2. Day-time progression
                    repository.advanceDayTime()

                    // 3. Passive city revenue & research from deployed AI units every 4 seconds
                    if (tickCount % 4 == 0) {
                        val totalDeployed = current.cityDistricts.sumOf { it.totalDeployedAiUnits }
                        if (totalDeployed > 0) {
                            val passiveRevenue = (totalDeployed * 55L).coerceAtLeast(60L)
                            val passiveRp = (totalDeployed / 3L).coerceAtLeast(1L)
                            repository.claimRevenueTick(passiveRevenue, passiveRp)
                        }
                    }
                }
            }
        }
    }

    fun startBuildingRobot(category: RobotCategory, customName: String = ""): Boolean {
        return repository.startRobotProduction(category, customName, null)
    }

    fun completeTesting(robotId: String, score: Int, grade: TestGrade) {
        repository.completeTesting(robotId, score, grade)
    }

    fun deliverRobotToCustomer(orderId: String, robotId: String): Boolean {
        return repository.deliverRobotToCustomer(orderId, robotId)
    }

    fun unlockTechNode(techId: String): Boolean {
        return repository.unlockTechNode(techId)
    }

    fun trainAiPerk(robotId: String, perkName: String, rpCost: Long): Boolean {
        return repository.trainAiBrainPerks(robotId, perkName, rpCost)
    }

    fun updateAiBrain(robotId: String, config: AiBrainConfig) {
        repository.updateAiBrainParameters(robotId, config)
    }

    // Tycoon Management
    fun advanceDay(): DailyReport {
        return repository.advanceDay()
    }

    fun setSimulationSpeed(speed: Int) {
        repository.setSimulationSpeed(speed)
    }

    fun hireEmployee(candidateId: String): Boolean {
        return repository.hireEmployee(candidateId)
    }

    fun fireEmployee(employeeId: String) {
        repository.fireEmployee(employeeId)
    }

    fun trainEmployee(employeeId: String): Boolean {
        return repository.trainEmployee(employeeId)
    }

    fun upgradeHq(): Boolean {
        return repository.upgradeHq()
    }

    fun executeCeoAction(action: CeoActionType): Boolean {
        return repository.executeCeoAction(action)
    }

    fun claimAchievementReward(achievementId: String): Boolean {
        return repository.claimAchievementReward(achievementId)
    }
}
