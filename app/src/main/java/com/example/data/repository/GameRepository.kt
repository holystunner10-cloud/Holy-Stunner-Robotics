package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

enum class CeoActionType(val title: String, val cost: Long, val rpBonus: Long, val hypeBonus: Int, val description: String, val iconName: String) {
    KEYNOTE_PRESENTATION("Host Global Keynote Event", 45000L, 120L, 25, "Unveil future tech live on stream (Elon/Steve Jobs style). Huge brand hype & tech interest!", "Campaign"),
    WEEKEND_CRUNCH_SPRINT("Hands-on Engineering Sprint", 15000L, 200L, 5, "Founder joins factory floor directly to supercharge prototype builds and firmware!", "Bolt"),
    VIRAL_TECH_DEMO("Post Viral Prototype Video", 8000L, 60L, 18, "Share raw test footage of autonomous AI drift or spatula precision on social media!", "Share"),
    METROPOLIS_CHARITY_GRANT("Metropolis Clean City Grant", 50000L, 80L, 30, "Donate smart bikes and solar bots to local schools & parks. Skyrockets citizen happiness!", "VolunteerActivism")
}

data class GameState(
    val companyCapital: Long = 2000000L, // Starting capital: $2 Million as requested!
    val researchPoints: Long = 250L,
    val brandReputation: Int = 85, // 0 to 100
    val cityHypeLevel: Int = 60,   // 0 to 100 GTA-style brand heat
    val companyTier: String = "Solo Garage Innovator",
    val factorySlotsTotal: Int = 3,
    val currentDay: Int = 1,
    val dayPhase: DayPhase = DayPhase.MORNING,
    val dayProgress: Float = 0.25f,
    val simulationSpeed: Int = 1, // 1 = 1x, 2 = 2x, 5 = 5x, 0 = pause
    val ceoProfile: CeoProfile = CeoProfile(),
    val employees: List<Employee> = emptyList(), // Starts working ALONE!
    val availableCandidates: List<Employee> = emptyList(),
    val activeRobots: List<RobotUnit> = emptyList(),
    val customerOrders: List<CustomerOrder> = emptyList(),
    val customerReviews: List<CustomerReview> = emptyList(),
    val cityDistricts: List<CityDistrict> = emptyList(),
    val techNodes: List<TechNode> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val newsFeed: List<NewsItem> = emptyList(),
    val activeEmergentEvent: EmergentEvent? = null,
    val lastDailyReport: DailyReport? = null,
    val dailyReportsHistory: List<DailyReport> = emptyList(),
    val totalProductsBuilt: Int = 0,
    val totalProductsDelivered: Int = 0,
    val totalRevenueEarned: Long = 0L,
    val soloBuiltCount: Int = 0,
    val activeBrainModuleEditing: AiBrainConfig? = null,
    val selectedRobotForTesting: RobotUnit? = null
) {
    val isSoloFounder: Boolean
        get() = employees.isEmpty()

    val totalDailyPayroll: Long
        get() = employees.sumOf { it.dailySalary }

    val dailyHqMaintenance: Long
        get() = ceoProfile.currentHqTier.dailyMaintenance

    val overallCityAiTransformation: Float
        get() {
            if (cityDistricts.isEmpty()) return 0f
            return cityDistricts.map { it.aiTransformationPercent }.average().toFloat()
        }

    val readyForDeliveryInventory: List<RobotUnit>
        get() = activeRobots.filter { it.isCompleted && !it.isDelivered }

    val buildingRobots: List<RobotUnit>
        get() = activeRobots.filter { !it.isCompleted }

    val hardwareStaffCount: Int
        get() = employees.count { it.department == Department.HARDWARE_ASSEMBLY }

    val aiStaffCount: Int
        get() = employees.count { it.department == Department.AI_RESEARCH }

    val qaStaffCount: Int
        get() = employees.count { it.department == Department.QA_TESTING }

    val marketingStaffCount: Int
        get() = employees.count { it.department == Department.SALES_MARKETING }

    val unclaimedAchievementsCount: Int
        get() = achievements.count { it.isUnlocked && !it.isClaimed }

    val totalRobotsDelivered: Int
        get() = totalProductsDelivered

    val cityHype: Int
        get() = cityHypeLevel
}

class GameRepository {

    private val _gameState = MutableStateFlow(createInitialGameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private fun createInitialGameState(): GameState {
        val initialDistricts = listOf(
            CityDistrict(
                id = "downtown",
                name = "Central Skyline Downtown",
                tagLine = "Bustling financial hub & high-density commerce corridors.",
                iconName = "LocationCity",
                population = 145000,
                aiTransformationPercent = 32f,
                aiCarsCount = 14,
                aiBikesCount = 20,
                deliveryDronesCount = 12,
                domesticBotsCount = 8,
                utilityBotsCount = 6,
                smartphonesCount = 45,
                smartTvsCount = 18,
                humanoidsCount = 4,
                citizenHappiness = 78,
                trafficCongestionReduction = 24,
                cleanEnergyIndex = 65,
                accentColorHex = 0xFF00BBF9
            ),
            CityDistrict(
                id = "suburbs",
                name = "Sunset Residential Palms",
                tagLine = "Family homes, lush gardens, neighborhood kitchens & smart living.",
                iconName = "Home",
                population = 92000,
                aiTransformationPercent = 25f,
                aiCarsCount = 8,
                aiBikesCount = 15,
                deliveryDronesCount = 18,
                domesticBotsCount = 22,
                utilityBotsCount = 4,
                smartphonesCount = 38,
                smartTvsCount = 30,
                humanoidsCount = 6,
                citizenHappiness = 82,
                trafficCongestionReduction = 18,
                cleanEnergyIndex = 70,
                accentColorHex = 0xFFFFB703
            ),
            CityDistrict(
                id = "tech_bay",
                name = "University & Innovation Bay",
                tagLine = "Student campuses, bike alleys, research institutes & startup incubators.",
                iconName = "School",
                population = 68000,
                aiTransformationPercent = 45f,
                aiCarsCount = 10,
                aiBikesCount = 42,
                deliveryDronesCount = 25,
                domesticBotsCount = 15,
                utilityBotsCount = 8,
                smartphonesCount = 62,
                smartTvsCount = 24,
                humanoidsCount = 10,
                citizenHappiness = 89,
                trafficCongestionReduction = 48,
                cleanEnergyIndex = 82,
                accentColorHex = 0xFF00F5D4
            ),
            CityDistrict(
                id = "logistics_port",
                name = "Aero Logistics & Port Harbor",
                tagLine = "Freight sky-corridors, automated docks & regional distribution center.",
                iconName = "FlightTakeoff",
                population = 41000,
                aiTransformationPercent = 38f,
                aiCarsCount = 12,
                aiBikesCount = 6,
                deliveryDronesCount = 35,
                domesticBotsCount = 5,
                utilityBotsCount = 16,
                smartphonesCount = 20,
                smartTvsCount = 12,
                humanoidsCount = 14,
                citizenHappiness = 76,
                trafficCongestionReduction = 30,
                cleanEnergyIndex = 74,
                accentColorHex = 0xFF9D4EDD
            ),
            CityDistrict(
                id = "green_industry",
                name = "Solaris Industrial Quarter",
                tagLine = "Clean energy solar farms, modern fabrication plants & smart grid utilities.",
                iconName = "PrecisionManufacturing",
                population = 35000,
                aiTransformationPercent = 28f,
                aiCarsCount = 6,
                aiBikesCount = 8,
                deliveryDronesCount = 14,
                domesticBotsCount = 4,
                utilityBotsCount = 22,
                smartphonesCount = 16,
                smartTvsCount = 8,
                humanoidsCount = 18,
                citizenHappiness = 74,
                trafficCongestionReduction = 20,
                cleanEnergyIndex = 88,
                accentColorHex = 0xFF06D6A0
            )
        )

        val initialOrders = listOf(
            CustomerOrder(
                id = "ord_01",
                citizenName = "Chef Anton Rousseau",
                profession = "Executive Chef, L'Étoile Bistro",
                avatarEmoji = "👨‍🍳",
                targetCategory = RobotCategory.CHEF_DOMESTIC,
                storyDescription = "Our dinner rush is overwhelming. We need a precision ChefBot with flawless fine-motor spatula skills to handle our sauté & pastry station!",
                districtId = "downtown",
                districtName = "Central Skyline Downtown",
                paymentReward = 16500L,
                bonusForHighGrade = 4500L,
                requiredMinimumGrade = TestGrade.GRADE_B
            ),
            CustomerOrder(
                id = "ord_02",
                citizenName = "Mayor Sophia Vance",
                profession = "Metropolis City Transportation Board",
                avatarEmoji = "👩‍💼",
                targetCategory = RobotCategory.AI_AUTO_CAR,
                storyDescription = "We are deploying the first fleet of Holy Stunner HyperDrive autonomous AI vehicles to eliminate downtown traffic jams and zero out collisions!",
                districtId = "downtown",
                districtName = "Central Skyline Downtown",
                paymentReward = 95000L,
                bonusForHighGrade = 22000L,
                requiredMinimumGrade = TestGrade.GRADE_A,
                isVipOrder = true
            ),
            CustomerOrder(
                id = "ord_03",
                citizenName = "Leo Chen",
                profession = "Software Student & Bike Commuter",
                avatarEmoji = "🚴",
                targetCategory = RobotCategory.AI_SMART_BIKE,
                storyDescription = "Tired of traffic gridlock. I want a self-balancing Stunner Pulse CyberBike that can weave through campus bike paths safely.",
                districtId = "tech_bay",
                districtName = "University & Innovation Bay",
                paymentReward = 6200L,
                bonusForHighGrade = 1500L,
                requiredMinimumGrade = TestGrade.GRADE_C
            ),
            CustomerOrder(
                id = "ord_04",
                citizenName = "Dr. Maya Lin",
                profession = "Chief Medical Director, Nova Health",
                avatarEmoji = "👩‍⚕️",
                targetCategory = RobotCategory.DELIVERY_DRONE,
                storyDescription = "Urgent need for an AeroDrop Drone capable of navigating wind shear to deliver donor organs and blood plasma between hospital rooftops!",
                districtId = "tech_bay",
                districtName = "University & Innovation Bay",
                paymentReward = 11200L,
                bonusForHighGrade = 3000L,
                requiredMinimumGrade = TestGrade.GRADE_B
            ),
            CustomerOrder(
                id = "ord_05",
                citizenName = "Alex Thorne",
                profession = "Tech Streamer & VR Enthusiast",
                avatarEmoji = "🎮",
                targetCategory = RobotCategory.AI_SMART_TV,
                storyDescription = "Setting up a future smart living room studio. Need the Stunner Vision 8K HoloTV with spatial neural projection for live city feeds!",
                districtId = "suburbs",
                districtName = "Sunset Residential Palms",
                paymentReward = 9400L,
                bonusForHighGrade = 2100L,
                requiredMinimumGrade = TestGrade.GRADE_B
            ),
            CustomerOrder(
                id = "ord_06",
                citizenName = "Chloe Zhao",
                profession = "Venture Partner, Silicon Horizon",
                avatarEmoji = "📱",
                targetCategory = RobotCategory.AI_SMARTPHONE,
                storyDescription = "Looking for the Stunner CyberPhone Pro with offline neural AI assistant to coordinate investments without cloud latency.",
                districtId = "downtown",
                districtName = "Central Skyline Downtown",
                paymentReward = 4500L,
                bonusForHighGrade = 1200L,
                requiredMinimumGrade = TestGrade.GRADE_B
            ),
            CustomerOrder(
                id = "ord_07",
                citizenName = "Captain Ray Donovan",
                profession = "Harbor Logistics Director",
                avatarEmoji = "👷",
                targetCategory = RobotCategory.AI_CYBER_TRUCK,
                storyDescription = "We need the Stunner CyberTruck heavy exoskeleton to transport battery container trailers across the freight port!",
                districtId = "green_industry",
                districtName = "Solaris Industrial Quarter",
                paymentReward = 135000L,
                bonusForHighGrade = 30000L,
                requiredMinimumGrade = TestGrade.GRADE_A,
                isVipOrder = true
            )
        )

        val initialReviews = listOf(
            CustomerReview(
                id = "rev_01",
                customerName = "Evelyn Sterling",
                profession = "Architectural Designer",
                avatarEmoji = "👩‍🎨",
                robotDelivered = "ChefBot Omni-9 (Grade S+)",
                ratingStars = 5,
                reviewText = "Holy Stunner Robotics changed my life! The ChefBot makes French crêpes every morning with zero mess. The precision dexterity is unreal!",
                districtName = "Sunset Residential Palms",
                tipAmount = 1450L
            ),
            CustomerReview(
                id = "rev_02",
                customerName = "Derrick Ross",
                profession = "Urban Commuter",
                avatarEmoji = "👨‍💻",
                robotDelivered = "Stunner Pulse Bike (Grade A)",
                ratingStars = 5,
                reviewText = "My daily 40-minute commute dropped to 12 minutes! The AI gyro stability avoids potholes and automatically syncs with green traffic lights.",
                districtName = "University & Innovation Bay",
                tipAmount = 850L
            )
        )

        val initialCandidates = listOf(
            Employee(
                id = "cand_01",
                name = "Dr. Aris Thorne",
                roleTitle = "Senior Neural Architect",
                avatarEmoji = "🧑‍🔬",
                department = Department.AI_RESEARCH,
                level = 1,
                skillRating = 88,
                dailySalary = 1200L,
                productivityBoostPercent = 25,
                morale = 100,
                traitDescription = "Ex-MIT AI researcher specializing in edge transformer models."
            ),
            Employee(
                id = "cand_02",
                name = "Kaito Tanaka",
                roleTitle = "Mechatronics Master",
                avatarEmoji = "👨‍🔧",
                department = Department.HARDWARE_ASSEMBLY,
                level = 1,
                skillRating = 92,
                dailySalary = 1400L,
                productivityBoostPercent = 30,
                morale = 100,
                traitDescription = "Precision roboticist with experience at Tesla Gigafactory assembly."
            ),
            Employee(
                id = "cand_03",
                name = "Elena Rostova",
                roleTitle = "Chief QA Inspector",
                avatarEmoji = "👩‍💻",
                department = Department.QA_TESTING,
                level = 1,
                skillRating = 85,
                dailySalary = 950L,
                productivityBoostPercent = 20,
                morale = 100,
                traitDescription = "Relentless perfectionist who spots micro-calibration bugs."
            ),
            Employee(
                id = "cand_04",
                name = "Marcus Sterling",
                roleTitle = "Global PR & Brand VP",
                avatarEmoji = "👔",
                department = Department.SALES_MARKETING,
                level = 1,
                skillRating = 89,
                dailySalary = 1350L,
                productivityBoostPercent = 28,
                morale = 100,
                traitDescription = "Master hype strategist known for sold-out consumer tech launches."
            ),
            Employee(
                id = "cand_05",
                name = "Zara Patel",
                roleTitle = "Aerospace Drone Engineer",
                avatarEmoji = "👩‍🚀",
                department = Department.HARDWARE_ASSEMBLY,
                level = 1,
                skillRating = 86,
                dailySalary = 1100L,
                productivityBoostPercent = 22,
                morale = 100,
                traitDescription = "Specializes in supersonic VTOL aerodynamics and composites."
            )
        )

        val initialAchievements = listOf(
            Achievement(
                id = "ach_solo_01",
                title = "Garage Pioneer",
                description = "Build 3 products solo as a lone founder before building a mega-team.",
                category = AchievementCategory.SOLO_PIONEER,
                iconName = "Person",
                rewardCapital = 50000L,
                rewardRp = 150L,
                rewardReputation = 5,
                currentProgress = 0,
                targetProgress = 3
            ),
            Achievement(
                id = "ach_solo_02",
                title = "The $3M Milestone",
                description = "Reach $3,000,000 in total company capital.",
                category = AchievementCategory.BILLIONAIRE_CLUB,
                iconName = "Paid",
                rewardCapital = 100000L,
                rewardRp = 200L,
                rewardReputation = 8,
                currentProgress = 2000000,
                targetProgress = 3000000
            ),
            Achievement(
                id = "ach_staff_01",
                title = "First Real Hire",
                description = "Hire your first employee to expand from a solo operation into a company.",
                category = AchievementCategory.TECH_TITAN,
                iconName = "PersonAdd",
                rewardCapital = 25000L,
                rewardRp = 100L,
                rewardReputation = 4,
                currentProgress = 0,
                targetProgress = 1
            ),
            Achievement(
                id = "ach_staff_02",
                title = "A-Team Syndicate",
                description = "Employ at least 4 specialist staff across all departments.",
                category = AchievementCategory.TECH_TITAN,
                iconName = "Groups",
                rewardCapital = 75000L,
                rewardRp = 300L,
                rewardReputation = 10,
                currentProgress = 0,
                targetProgress = 4
            ),
            Achievement(
                id = "ach_prod_phone",
                title = "Pocket Neural Revolution",
                description = "Manufacture & deliver Stunner Smartphones or Foldables to citizens.",
                category = AchievementCategory.PRODUCT_EMPIRE,
                iconName = "Smartphone",
                rewardCapital = 40000L,
                rewardRp = 150L,
                rewardReputation = 6,
                currentProgress = 0,
                targetProgress = 3
            ),
            Achievement(
                id = "ach_prod_tesla",
                title = "HyperDrive Fleet",
                description = "Deliver 3 Autonomous AI Cars or CyberTrucks across Metropolis.",
                category = AchievementCategory.PRODUCT_EMPIRE,
                iconName = "DirectionsCar",
                rewardCapital = 150000L,
                rewardRp = 400L,
                rewardReputation = 15,
                currentProgress = 0,
                targetProgress = 3
            ),
            Achievement(
                id = "ach_prod_spacex",
                title = "Falcon Aerospace StarLift",
                description = "Engineer & deliver sub-orbital heavy drones or aerospace logistics.",
                category = AchievementCategory.PRODUCT_EMPIRE,
                iconName = "RocketLaunch",
                rewardCapital = 250000L,
                rewardRp = 500L,
                rewardReputation = 20,
                currentProgress = 0,
                targetProgress = 2
            ),
            Achievement(
                id = "ach_city_01",
                title = "Cyber City 50%",
                description = "Elevate overall Metropolis City AI Transformation to over 50%.",
                category = AchievementCategory.METROPOLIS_AI,
                iconName = "LocationCity",
                rewardCapital = 120000L,
                rewardRp = 350L,
                rewardReputation = 12,
                currentProgress = 33,
                targetProgress = 50
            ),
            Achievement(
                id = "ach_city_02",
                title = "Singularity Metropolis 80%",
                description = "Transform the city into an autonomous AI utopia with 80%+ AI integration.",
                category = AchievementCategory.METROPOLIS_AI,
                iconName = "AutoAwesome",
                rewardCapital = 500000L,
                rewardRp = 1000L,
                rewardReputation = 25,
                currentProgress = 33,
                targetProgress = 80
            ),
            Achievement(
                id = "ach_quality_s",
                title = "Holy Stunner Perfection (S+)",
                description = "Score a Grade S+ on 3 different product testing simulations.",
                category = AchievementCategory.SOLO_PIONEER,
                iconName = "Star",
                rewardCapital = 80000L,
                rewardRp = 250L,
                rewardReputation = 10,
                currentProgress = 0,
                targetProgress = 3
            )
        )

        val initialTech = listOf(
            TechNode(
                id = "tech_neural_synapse",
                title = "Quantum Synapse Matrix v2",
                category = "AI Brain",
                description = "Boosts AI brain intelligence training efficiency and reduces decision latency by 35%.",
                researchCost = 80L,
                isUnlocked = true,
                iconName = "Psychology",
                unlockEffect = "+15% AI IQ on all newly engineered bots & devices"
            ),
            TechNode(
                id = "tech_graphene_rotors",
                title = "Silent Graphene Drone Rotors",
                category = "Aerospace",
                description = "Ultra-lightweight aerodynamic rotors for AeroDrop Drones with 50% less acoustic noise.",
                researchCost = 150L,
                isUnlocked = false,
                iconName = "Air",
                unlockEffect = "+25% Wind resistance & +$1,500 delivery bonus"
            ),
            TechNode(
                id = "tech_mesh_car_network",
                title = "HyperDrive Fleet Mesh V2X",
                category = "Autonomous Transit",
                description = "Enables AI cars to communicate directly with traffic grids and AI smart bikes.",
                researchCost = 250L,
                isUnlocked = false,
                iconName = "Share",
                unlockEffect = "Increases city traffic reduction by +30%"
            ),
            TechNode(
                id = "tech_michelin_db",
                title = "Culinary Michelin Neural DB",
                category = "Fine Motor",
                description = "Deep learning library of 10,000 gourmet recipes and molecular gastronomy techniques.",
                researchCost = 180L,
                isUnlocked = false,
                iconName = "MenuBook",
                unlockEffect = "ChefBots automatically achieve +1 Grade tier in QA testing"
            ),
            TechNode(
                id = "tech_titan_exoskeleton",
                title = "Titanium Cyber-Chassis",
                category = "Hardware",
                description = "Reinforced lightweight chassis with regenerative solar kinetic coating.",
                researchCost = 320L,
                isUnlocked = false,
                iconName = "Shield",
                unlockEffect = "-20% product build cost across all lines"
            ),
            TechNode(
                id = "tech_automated_assembly",
                title = "Gigafactory Dual Robotic Lines",
                category = "Industry",
                description = "Expands factory assembly lines from 3 to 6 simultaneous manufacturing bays.",
                researchCost = 450L,
                isUnlocked = false,
                iconName = "Factory",
                unlockEffect = "Unlocks +3 extra factory production slots"
            ),
            TechNode(
                id = "tech_quantum_phone_chip",
                title = "Neural Edge Quantum Silicon",
                category = "Mobile Tech",
                description = "Dedicated on-device neural processing unit for Stunner smartphones and smart TVs.",
                researchCost = 380L,
                isUnlocked = false,
                iconName = "Memory",
                unlockEffect = "Doubles smartphone profit margin & citizen reviews"
            ),
            TechNode(
                id = "tech_starbase_propulsion",
                title = "Falcon Starbase Thrusters",
                category = "Aerospace",
                description = "Methane-oxygen clean thrusters for hypersonic orbital delivery drones.",
                researchCost = 600L,
                isUnlocked = false,
                iconName = "RocketLaunch",
                unlockEffect = "Unlocks Falcon Heavy sub-orbital aerospace production"
            )
        )

        val initialNews = listOf(
            NewsItem(
                id = "news_01",
                headline = "Holy Stunner Robotics launches with $2,000,000 capital in Metropolis garage!",
                category = "Industry",
                isBreaking = true
            ),
            NewsItem(
                id = "news_02",
                headline = "City Mayor welcomes next-gen AI: 'From smartphones to autonomous cars, this changes Metropolis!'",
                category = "City Life",
                isBreaking = false
            ),
            NewsItem(
                id = "news_03",
                headline = "Stock market buzzed by rumors of upcoming Holy Stunner Gigafactory expansion.",
                category = "Markets",
                isBreaking = false
            )
        )

        val initialRobots = listOf(
            RobotUnit(
                id = UUID.randomUUID().toString(),
                serialNumber = "HSR-PHONE-001",
                category = RobotCategory.AI_SMARTPHONE,
                customName = "Stunner CyberPhone Prototype",
                testGrade = TestGrade.GRADE_A,
                testScore = 94,
                isCompleted = true,
                isTested = true,
                isDelivered = false,
                buildProgress = 1.0f,
                productionCost = RobotCategory.AI_SMARTPHONE.baseCost
            ),
            RobotUnit(
                id = UUID.randomUUID().toString(),
                serialNumber = "HSR-BIKE-002",
                category = RobotCategory.AI_SMART_BIKE,
                customName = "Pulse CyberBike Prototype",
                testGrade = TestGrade.UNTESTED,
                testScore = 0,
                isCompleted = true,
                isTested = false,
                isDelivered = false,
                buildProgress = 1.0f,
                productionCost = RobotCategory.AI_SMART_BIKE.baseCost
            )
        )

        return GameState(
            companyCapital = 2000000L, // $2 Million
            researchPoints = 250L,
            brandReputation = 85,
            cityHypeLevel = 60,
            companyTier = "Solo Garage Innovator",
            factorySlotsTotal = 3,
            currentDay = 1,
            dayPhase = DayPhase.MORNING,
            dayProgress = 0.25f,
            simulationSpeed = 1,
            ceoProfile = CeoProfile(),
            employees = emptyList(), // Starting alone!
            availableCandidates = initialCandidates,
            activeRobots = initialRobots,
            customerOrders = initialOrders,
            customerReviews = initialReviews,
            cityDistricts = initialDistricts,
            techNodes = initialTech,
            achievements = initialAchievements,
            newsFeed = initialNews,
            activeEmergentEvent = EmergentEvent(
                id = "evt_01",
                title = "Metropolis AI Tech Expo 2026",
                description = "Tech enthusiasts & enterprise buyers are swarming Metropolis! Fulfilling VIP orders awards +$15,000 bonus.",
                affectedDistrictName = "Central Skyline Downtown",
                categoryTarget = RobotCategory.AI_AUTO_CAR,
                rewardBonus = 15000L,
                iconName = "Celebration"
            )
        )
    }

    fun startRobotProduction(category: RobotCategory, customName: String, customBrain: AiBrainConfig?): Boolean {
        val current = _gameState.value
        val cost = category.baseCost
        if (current.companyCapital < cost) return false
        if (current.buildingRobots.size >= current.factorySlotsTotal) return false

        val newRobot = RobotUnit(
            id = UUID.randomUUID().toString(),
            serialNumber = "HSR-${category.name.take(5)}-${(100..999).random()}",
            category = category,
            customName = if (customName.isNotBlank()) customName else "${category.title} #${(1..99).random()}",
            brainConfig = customBrain ?: AiBrainConfig.defaultFor(category),
            testGrade = TestGrade.UNTESTED,
            isCompleted = false,
            isTested = false,
            isDelivered = false,
            buildProgress = 0f,
            productionCost = cost
        )

        _gameState.update { state ->
            val nextTotal = state.totalProductsBuilt + 1
            val nextSolo = if (state.isSoloFounder) state.soloBuiltCount + 1 else state.soloBuiltCount
            val updatedAchievements = updateAchievementsProgress(
                state.achievements,
                soloCount = nextSolo,
                capital = state.companyCapital - cost,
                teamSize = state.employees.size,
                cityTransform = state.overallCityAiTransformation.toInt()
            )

            state.copy(
                companyCapital = state.companyCapital - cost,
                activeRobots = state.activeRobots + newRobot,
                totalProductsBuilt = nextTotal,
                soloBuiltCount = nextSolo,
                achievements = updatedAchievements
            )
        }
        return true
    }

    fun advanceRobotBuildProgress(robotId: String, deltaProgress: Float) {
        _gameState.update { state ->
            // Apply employee hardware boost + solo founder bonus
            val hwBoost = 1f + (state.hardwareStaffCount * 0.25f)
            val soloBonus = if (state.isSoloFounder) state.ceoProfile.soloCraftingBonus else 1.0f
            val actualDelta = deltaProgress * hwBoost * soloBonus

            val updated = state.activeRobots.map { bot ->
                if (bot.id == robotId && !bot.isCompleted) {
                    val nextProg = (bot.buildProgress + actualDelta).coerceAtMost(1f)
                    val isDone = nextProg >= 1f
                    bot.copy(
                        buildProgress = nextProg,
                        isCompleted = isDone
                    )
                } else bot
            }
            state.copy(activeRobots = updated)
        }
    }

    fun completeTesting(robotId: String, score: Int, grade: TestGrade) {
        _gameState.update { state ->
            val earnedRP = when (grade) {
                TestGrade.GRADE_S -> 45L
                TestGrade.GRADE_A -> 30L
                TestGrade.GRADE_B -> 20L
                TestGrade.GRADE_C -> 12L
                TestGrade.UNTESTED -> 0L
            }
            val updated = state.activeRobots.map { bot ->
                if (bot.id == robotId) {
                    bot.copy(
                        isTested = true,
                        testScore = score,
                        testGrade = grade,
                        qualityScore = (score * 1.1f).toInt().coerceIn(60, 100)
                    )
                } else bot
            }

            val sGradeCount = updated.count { it.testGrade == TestGrade.GRADE_S }
            val updatedAchievements = updateAchievementsProgress(
                state.achievements,
                soloCount = state.soloBuiltCount,
                capital = state.companyCapital,
                teamSize = state.employees.size,
                cityTransform = state.overallCityAiTransformation.toInt(),
                sGrades = sGradeCount
            )

            state.copy(
                activeRobots = updated,
                researchPoints = state.researchPoints + earnedRP,
                achievements = updatedAchievements
            )
        }
    }

    fun deliverRobotToCustomer(orderId: String, robotId: String): Boolean {
        val current = _gameState.value
        val order = current.customerOrders.find { it.id == orderId } ?: return false
        val robot = current.activeRobots.find { it.id == robotId } ?: return false

        if (!robot.isCompleted || robot.isDelivered) return false
        if (robot.category != order.targetCategory) return false

        val marketingBonus = (current.marketingStaffCount * 0.10f) * order.paymentReward
        val baseEarned = order.paymentReward + marketingBonus.toLong()
        val bonus = if (robot.testGrade == TestGrade.GRADE_S || robot.testGrade == TestGrade.GRADE_A) {
            order.bonusForHighGrade
        } else 0L
        val eventBonus = if (current.activeEmergentEvent?.categoryTarget == robot.category) {
            current.activeEmergentEvent.rewardBonus
        } else 0L

        val totalEarnings = baseEarned + bonus + eventBonus
        val tip = (totalEarnings * (robot.testGrade.multiplier - 0.7f).coerceAtLeast(0.1f) * 0.15f).toLong()

        val reviewComments = mapOf(
            RobotCategory.CHEF_DOMESTIC to listOf(
                "Incredible cooking! Our dinner rush ran like clockwork and customers raved about the flawless soufflés.",
                "The knife skills and cleanliness are astounding. Holy Stunner has transformed our kitchen!",
                "Amazing culinary AI. It even tuned the spice levels to our family's exact preference!"
            ),
            RobotCategory.TITAN_HUMANOID to listOf(
                "The Optimus Titan is breathtaking. It guards our premises and handles heavy inventory effortlessly!",
                "Incredible bipedal balance and polite conversational AI. Truly the future of robotics!",
                "Helped our elderly family member carry groceries and navigate stairs safely."
            ),
            RobotCategory.AI_SMARTPHONE to listOf(
                "The Stunner CyberPhone has replaced my laptop! Offline neural co-pilot solves complex math instantly.",
                "Zero battery drain with the quantum chip and the holographic screen is mind-blowing!",
                "Best phone I've ever owned. The camera AI predicts perfect lighting in pitch darkness."
            ),
            RobotCategory.AI_FOLDABLE_PHONE to listOf(
                "The flexible glass screen is pure magic. Satellite AI connects even in deep mountain trails!",
                "Stunning craftsmanship. Everyone on the subway asks where I bought this fold phone!"
            ),
            RobotCategory.AI_AUTO_CAR to listOf(
                "Smooth, whisper-quiet autonomous driving. It handled heavy downtown rain without a single hiccup.",
                "Zero commute stress now! The AI route optimization shaved 25 minutes off my daily trip.",
                "Safety reflexes are world-class. It detected a cyclist in the blind spot before I even saw them."
            ),
            RobotCategory.AI_CYBER_TRUCK to listOf(
                "Unstoppable power. Towed a 15-ton solar generator through rough terrain without sweating.",
                "The armored steel body looks like a spaceship from 2077. Super impressive engineering!",
                "Our logistics convoy operates on full autopilot now with zero downtime."
            ),
            RobotCategory.AI_SMART_BIKE to listOf(
                "The self-balancing gyro is magical. Zooming through university campus alleys feels like flying!",
                "No more parking hassles or sweat. The smart pedal assist and collision shield are perfection.",
                "Best commuter vehicle ever. It charges with regenerative braking on downhill slopes!"
            ),
            RobotCategory.AI_SMART_TV to listOf(
                "The 8K holographic depth turns my living room into an IMAX theater! Spatial audio is incredible.",
                "It automatically controls all my lights, blinds and thermostats through the central AI hub."
            ),
            RobotCategory.DELIVERY_DRONE to listOf(
                "Delivered critical medical packages in under 4 minutes across the city skyline!",
                "Gentle touchdown right on our rooftop landing pad in 30 knot winds. Marvelous engineering."
            ),
            RobotCategory.FALCON_ORBITAL_DRONE to listOf(
                "The Falcon StarLift delivers industrial freight across the entire metropolitan region in 7 minutes!",
                "SpaceX-level VTOL precision. Truly elevated our regional supply chain."
            ),
            RobotCategory.MEDICAL_CARE to listOf(
                "Such a gentle and attentive companion. Grandma loves having her morning tea and garden strolls.",
                "Real-time vitals monitoring gives our family complete peace of mind. Truly a lifesaver."
            ),
            RobotCategory.CONSTRUCTION_UTILITY to listOf(
                "Installed 40 solar panels in half the estimated time. Unstoppable torque and precision alignment.",
                "Resurfaced the warehouse track flawlessly. Robust hardware with an exceptionally smart brain."
            )
        )

        val starRating = when (robot.testGrade) {
            TestGrade.GRADE_S -> 5
            TestGrade.GRADE_A -> 5
            TestGrade.GRADE_B -> 4
            TestGrade.GRADE_C -> 3
            TestGrade.UNTESTED -> 2
        }

        val chosenComment = reviewComments[robot.category]?.random() ?: "Delivered on time and works exceptionally well!"
        val newReview = CustomerReview(
            id = UUID.randomUUID().toString(),
            customerName = order.citizenName,
            profession = order.profession,
            avatarEmoji = order.avatarEmoji,
            robotDelivered = "${robot.customName} (${robot.testGrade.gradeLetter})",
            ratingStars = starRating,
            reviewText = chosenComment,
            districtName = order.districtName,
            tipAmount = tip
        )

        val newCapital = current.companyCapital + totalEarnings + tip
        val newTotalDelivered = current.totalProductsDelivered + 1

        _gameState.update { state ->
            val updatedRobots = state.activeRobots.map { bot ->
                if (bot.id == robotId) bot.copy(isDelivered = true, assignedDistrictId = order.districtId) else bot
            }

            val updatedOrders = state.customerOrders.map { ord ->
                if (ord.id == orderId) ord.copy(isFulfilled = true, fulfilledRobotId = robotId, generatedReview = newReview) else ord
            }

            val updatedDistricts = state.cityDistricts.map { dist ->
                if (dist.id == order.districtId) {
                    val newCars = if (robot.category == RobotCategory.AI_AUTO_CAR || robot.category == RobotCategory.AI_CYBER_TRUCK) dist.aiCarsCount + 1 else dist.aiCarsCount
                    val newBikes = if (robot.category == RobotCategory.AI_SMART_BIKE) dist.aiBikesCount + 1 else dist.aiBikesCount
                    val newDrones = if (robot.category == RobotCategory.DELIVERY_DRONE || robot.category == RobotCategory.FALCON_ORBITAL_DRONE) dist.deliveryDronesCount + 1 else dist.deliveryDronesCount
                    val newDomestic = if (robot.category == RobotCategory.CHEF_DOMESTIC || robot.category == RobotCategory.MEDICAL_CARE) dist.domesticBotsCount + 1 else dist.domesticBotsCount
                    val newUtility = if (robot.category == RobotCategory.CONSTRUCTION_UTILITY) dist.utilityBotsCount + 1 else dist.utilityBotsCount
                    val newPhones = if (robot.category == RobotCategory.AI_SMARTPHONE || robot.category == RobotCategory.AI_FOLDABLE_PHONE) dist.smartphonesCount + 1 else dist.smartphonesCount
                    val newTvs = if (robot.category == RobotCategory.AI_SMART_TV) dist.smartTvsCount + 1 else dist.smartTvsCount
                    val newHumanoids = if (robot.category == RobotCategory.TITAN_HUMANOID) dist.humanoidsCount + 1 else dist.humanoidsCount

                    val newTransform = (dist.aiTransformationPercent + 4.0f).coerceAtMost(100f)
                    val newHappiness = (dist.citizenHappiness + 2).coerceAtMost(100)
                    val newTrafficRed = (dist.trafficCongestionReduction + (if (robot.category == RobotCategory.AI_AUTO_CAR || robot.category == RobotCategory.AI_SMART_BIKE) 3 else 1)).coerceAtMost(100)
                    val newCleanEnergy = (dist.cleanEnergyIndex + 2).coerceAtMost(100)

                    dist.copy(
                        aiTransformationPercent = newTransform,
                        aiCarsCount = newCars,
                        aiBikesCount = newBikes,
                        deliveryDronesCount = newDrones,
                        domesticBotsCount = newDomestic,
                        utilityBotsCount = newUtility,
                        smartphonesCount = newPhones,
                        smartTvsCount = newTvs,
                        humanoidsCount = newHumanoids,
                        citizenHappiness = newHappiness,
                        trafficCongestionReduction = newTrafficRed,
                        cleanEnergyIndex = newCleanEnergy
                    )
                } else dist
            }

            val newsTitle = "Citizen ${order.citizenName} receives ${robot.customName} for ${order.districtName}!"
            val updatedNews = listOf(
                NewsItem(
                    id = UUID.randomUUID().toString(),
                    headline = newsTitle,
                    category = "City Life"
                )
            ) + state.newsFeed.take(6)

            val updatedAchievements = updateAchievementsProgress(
                state.achievements,
                soloCount = state.soloBuiltCount,
                capital = newCapital,
                teamSize = state.employees.size,
                cityTransform = updatedDistricts.map { it.aiTransformationPercent }.average().toInt(),
                phoneDelivered = updatedRobots.count { (it.category == RobotCategory.AI_SMARTPHONE || it.category == RobotCategory.AI_FOLDABLE_PHONE) && it.isDelivered },
                carDelivered = updatedRobots.count { (it.category == RobotCategory.AI_AUTO_CAR || it.category == RobotCategory.AI_CYBER_TRUCK) && it.isDelivered },
                droneDelivered = updatedRobots.count { (it.category == RobotCategory.DELIVERY_DRONE || it.category == RobotCategory.FALCON_ORBITAL_DRONE) && it.isDelivered }
            )

            state.copy(
                companyCapital = newCapital,
                totalRevenueEarned = state.totalRevenueEarned + totalEarnings + tip,
                totalProductsDelivered = newTotalDelivered,
                brandReputation = (state.brandReputation + (if (starRating >= 4) 2 else 0)).coerceAtMost(100),
                cityHypeLevel = (state.cityHypeLevel + 3).coerceAtMost(100),
                activeRobots = updatedRobots,
                customerOrders = updatedOrders,
                customerReviews = listOf(newReview) + state.customerReviews,
                cityDistricts = updatedDistricts,
                newsFeed = updatedNews,
                achievements = updatedAchievements
            )
        }

        generateNewCustomerOrder()
        return true
    }

    fun hireEmployee(candidateId: String): Boolean {
        val current = _gameState.value
        val candidate = current.availableCandidates.find { it.id == candidateId } ?: return false
        if (current.employees.size >= current.ceoProfile.currentHqTier.capacity) return false
        if (current.companyCapital < candidate.dailySalary * 2) return false

        _gameState.update { state ->
            val updatedCandidates = state.availableCandidates.filter { it.id != candidateId }
            val updatedEmployees = state.employees + candidate

            val updatedAchievements = updateAchievementsProgress(
                state.achievements,
                soloCount = state.soloBuiltCount,
                capital = state.companyCapital,
                teamSize = updatedEmployees.size,
                cityTransform = state.overallCityAiTransformation.toInt()
            )

            val newTier = when {
                updatedEmployees.size >= 15 -> "Global AI Conglomerate"
                updatedEmployees.size >= 6 -> "Fast-Growing Tech Enterprise"
                updatedEmployees.size >= 1 -> "High-Growth AI Startup"
                else -> "Solo Garage Innovator"
            }

            state.copy(
                employees = updatedEmployees,
                availableCandidates = updatedCandidates,
                companyTier = newTier,
                achievements = updatedAchievements
            )
        }
        return true
    }

    fun fireEmployee(employeeId: String) {
        _gameState.update { state ->
            val updated = state.employees.filter { it.id != employeeId }
            state.copy(employees = updated)
        }
    }

    fun trainEmployee(employeeId: String): Boolean {
        val current = _gameState.value
        val emp = current.employees.find { it.id == employeeId } ?: return false
        if (current.companyCapital < emp.upgradeCost) return false

        _gameState.update { state ->
            val updatedEmployees = state.employees.map { e ->
                if (e.id == employeeId) {
                    e.copy(
                        level = e.level + 1,
                        skillRating = (e.skillRating + 5).coerceAtMost(100),
                        productivityBoostPercent = e.productivityBoostPercent + 8,
                        dailySalary = (e.dailySalary * 1.2f).toLong()
                    )
                } else e
            }
            state.copy(
                companyCapital = state.companyCapital - emp.upgradeCost,
                employees = updatedEmployees
            )
        }
        return true
    }

    fun upgradeHq(): Boolean {
        val current = _gameState.value
        val nextTier = when (current.ceoProfile.currentHqTier) {
            HqTier.SUBURBAN_GARAGE -> HqTier.INNOVATION_WAREHOUSE
            HqTier.INNOVATION_WAREHOUSE -> HqTier.SILICON_VALLEY_CAMPUS
            HqTier.SILICON_VALLEY_CAMPUS -> HqTier.GIGAFACTORY_STARBASE
            HqTier.GIGAFACTORY_STARBASE -> return false
        }

        if (current.companyCapital < nextTier.upgradeCost) return false

        _gameState.update { state ->
            val extraSlots = when (nextTier) {
                HqTier.INNOVATION_WAREHOUSE -> 2
                HqTier.SILICON_VALLEY_CAMPUS -> 4
                HqTier.GIGAFACTORY_STARBASE -> 8
                else -> 0
            }

            val updatedNews = listOf(
                NewsItem(
                    id = UUID.randomUUID().toString(),
                    headline = "Holy Stunner Robotics upgrades headquarters to ${nextTier.title}!",
                    category = "HQ Expansion",
                    isBreaking = true
                )
            ) + state.newsFeed

            state.copy(
                companyCapital = state.companyCapital - nextTier.upgradeCost,
                ceoProfile = state.ceoProfile.copy(currentHqTier = nextTier),
                factorySlotsTotal = state.factorySlotsTotal + extraSlots,
                brandReputation = (state.brandReputation + 5).coerceAtMost(100),
                cityHypeLevel = (state.cityHypeLevel + 15).coerceAtMost(100),
                newsFeed = updatedNews
            )
        }
        return true
    }

    fun advanceDay(): DailyReport {
        val current = _gameState.value
        val dayNum = current.currentDay

        // Calculate financials
        val payroll = current.totalDailyPayroll
        val maintenance = current.dailyHqMaintenance
        val aiRp = 20L + (current.aiStaffCount * 45L)

        // Passive sales from deployed units in city
        val totalDeployed = current.cityDistricts.sumOf { it.totalDeployedAiUnits }
        val passiveCityRev = (totalDeployed * 75L)
        val net = passiveCityRev - (payroll + maintenance)

        val newCapital = (current.companyCapital + net).coerceAtLeast(0L)
        val newRp = current.researchPoints + aiRp
        val nextDay = dayNum + 1

        val report = DailyReport(
            dayNumber = dayNum,
            grossRevenue = passiveCityRev,
            payrollPaid = payroll,
            maintenancePaid = maintenance,
            netProfit = net,
            researchEarned = aiRp,
            productsDeliveredCount = current.totalProductsDelivered,
            cityAiGrowth = 1.8f,
            citizenReviewHighlight = current.customerReviews.firstOrNull()?.reviewText ?: "Citizens are loving the smart city innovations!",
            dailyNewsHeadline = "Day $nextDay begins: Metropolis AI adoption reaches new heights!"
        )

        _gameState.update { state ->
            val updatedAchievements = updateAchievementsProgress(
                state.achievements,
                soloCount = state.soloBuiltCount,
                capital = newCapital,
                teamSize = state.employees.size,
                cityTransform = state.overallCityAiTransformation.toInt()
            )

            // Refresh candidate pool
            val newCandidates = generateNewCandidates(state.availableCandidates)

            state.copy(
                currentDay = nextDay,
                dayPhase = DayPhase.MORNING,
                dayProgress = 0.25f,
                companyCapital = newCapital,
                researchPoints = newRp,
                lastDailyReport = report,
                dailyReportsHistory = listOf(report) + state.dailyReportsHistory.take(10),
                availableCandidates = newCandidates,
                achievements = updatedAchievements
            )
        }

        return report
    }

    fun advanceDayTime() {
        _gameState.update { state ->
            val nextProg = state.dayProgress + (0.02f * state.simulationSpeed)
            if (nextProg >= 1.0f) {
                // Trigger auto end of day
                state.copy(dayProgress = 0f, dayPhase = DayPhase.NIGHT)
            } else {
                val nextPhase = when {
                    nextProg < 0.35f -> DayPhase.MORNING
                    nextProg < 0.65f -> DayPhase.AFTERNOON
                    nextProg < 0.88f -> DayPhase.EVENING
                    else -> DayPhase.NIGHT
                }
                state.copy(dayProgress = nextProg, dayPhase = nextPhase)
            }
        }
    }

    fun setSimulationSpeed(speed: Int) {
        _gameState.update { it.copy(simulationSpeed = speed) }
    }

    fun executeCeoAction(actionType: CeoActionType): Boolean {
        val current = _gameState.value
        if (current.companyCapital < actionType.cost) return false

        _gameState.update { state ->
            val updatedNews = listOf(
                NewsItem(
                    id = UUID.randomUUID().toString(),
                    headline = "CEO Action: ${actionType.title}! ${actionType.description}",
                    category = "Executive",
                    isBreaking = true
                )
            ) + state.newsFeed

            state.copy(
                companyCapital = state.companyCapital - actionType.cost,
                researchPoints = state.researchPoints + actionType.rpBonus,
                cityHypeLevel = (state.cityHypeLevel + actionType.hypeBonus).coerceAtMost(100),
                brandReputation = (state.brandReputation + 3).coerceAtMost(100),
                newsFeed = updatedNews
            )
        }
        return true
    }

    fun claimAchievementReward(achievementId: String): Boolean {
        val current = _gameState.value
        val ach = current.achievements.find { it.id == achievementId } ?: return false
        if (!ach.isUnlocked || ach.isClaimed) return false

        _gameState.update { state ->
            val updated = state.achievements.map {
                if (it.id == achievementId) it.copy(isClaimed = true) else it
            }
            state.copy(
                companyCapital = state.companyCapital + ach.rewardCapital,
                researchPoints = state.researchPoints + ach.rewardRp,
                brandReputation = (state.brandReputation + ach.rewardReputation).coerceAtMost(100),
                achievements = updated
            )
        }
        return true
    }

    fun unlockTechNode(techId: String): Boolean {
        val current = _gameState.value
        val node = current.techNodes.find { it.id == techId } ?: return false
        if (node.isUnlocked) return false
        if (current.researchPoints < node.researchCost) return false

        _gameState.update { state ->
            var newSlots = state.factorySlotsTotal
            if (techId == "tech_automated_assembly") {
                newSlots += 3
            }
            val updatedNodes = state.techNodes.map {
                if (it.id == techId) it.copy(isUnlocked = true) else it
            }
            state.copy(
                researchPoints = state.researchPoints - node.researchCost,
                factorySlotsTotal = newSlots,
                techNodes = updatedNodes
            )
        }
        return true
    }

    fun trainAiBrainPerks(robotId: String, perk: String, rpCost: Long): Boolean {
        val current = _gameState.value
        if (current.researchPoints < rpCost) return false

        _gameState.update { state ->
            val updatedRobots = state.activeRobots.map { bot ->
                if (bot.id == robotId) {
                    val currentPerks = bot.brainConfig.activePerks
                    if (!currentPerks.contains(perk)) {
                        bot.copy(
                            brainConfig = bot.brainConfig.copy(
                                activePerks = currentPerks + perk
                            )
                        )
                    } else bot
                } else bot
            }
            state.copy(
                researchPoints = state.researchPoints - rpCost,
                activeRobots = updatedRobots
            )
        }
        return true
    }

    fun updateAiBrainParameters(robotId: String, newConfig: AiBrainConfig) {
        _gameState.update { state ->
            val updated = state.activeRobots.map { bot ->
                if (bot.id == robotId) bot.copy(brainConfig = newConfig) else bot
            }
            state.copy(activeRobots = updated)
        }
    }

    fun claimRevenueTick(amount: Long, rp: Long) {
        _gameState.update { state ->
            state.copy(
                companyCapital = state.companyCapital + amount,
                researchPoints = state.researchPoints + rp
            )
        }
    }

    private fun generateNewCustomerOrder() {
        val names = listOf(
            Pair("Marcus Sterling", "👨‍💼 Logistics VP"),
            Pair("Chef Gabriela Sol", "👩‍🍳 Bakery Owner"),
            Pair("Aiden Park", "🧑‍💻 Tech Founder"),
            Pair("Nurse Clara Santos", "👩‍⚕️ Community Clinic"),
            Pair("Sora Takahashi", "🚴 Eco Urbanite"),
            Pair("Liam Vance", "👨‍🚒 District Fire Chief"),
            Pair("Zara Sterling", "👩‍💼 Venture Capitalist"),
            Pair("Oliver Queen", "🏢 Real Estate Titan")
        )
        val districts = listOf(
            Pair("downtown", "Central Skyline Downtown"),
            Pair("suburbs", "Sunset Residential Palms"),
            Pair("tech_bay", "University & Innovation Bay"),
            Pair("logistics_port", "Aero Logistics & Port Harbor"),
            Pair("green_industry", "Solaris Industrial Quarter")
        )
        val randomName = names.random()
        val randomDistrict = districts.random()
        val randomCategory = RobotCategory.values().random()

        val isVip = randomCategory.baseCost > 30000

        val newOrder = CustomerOrder(
            id = "ord_${UUID.randomUUID().toString().take(6)}",
            citizenName = randomName.first,
            profession = randomName.second,
            avatarEmoji = randomCategory.iconName,
            targetCategory = randomCategory,
            storyDescription = "Looking for high-performance ${randomCategory.title} to transform our daily life in ${randomDistrict.second}!",
            districtId = randomDistrict.first,
            districtName = randomDistrict.second,
            paymentReward = (randomCategory.basePrice * 1.25f).toLong(),
            bonusForHighGrade = (randomCategory.basePrice * 0.35f).toLong(),
            requiredMinimumGrade = if (isVip) TestGrade.GRADE_A else TestGrade.GRADE_B,
            isVipOrder = isVip
        )

        _gameState.update { state ->
            state.copy(
                customerOrders = state.customerOrders + newOrder
            )
        }
    }

    private fun generateNewCandidates(existing: List<Employee>): List<Employee> {
        if (existing.size >= 4) return existing
        val pool = listOf(
            Employee("c_${UUID.randomUUID().toString().take(4)}", "Dr. Nova Vance", "Quantum AI Lead", "👩‍🔬", Department.AI_RESEARCH, 1, 91, 1500L, 30, 100, "Expert in deep neural reinforcement and robotics brains."),
            Employee("c_${UUID.randomUUID().toString().take(4)}", "Julian Drake", "Factory Chief", "👨‍🏭", Department.HARDWARE_ASSEMBLY, 1, 88, 1300L, 26, 100, "Streamlines high-volume manufacturing lines."),
            Employee("c_${UUID.randomUUID().toString().take(4)}", "Mia Lin", "Test Simulation Engineer", "👩‍🔧", Department.QA_TESTING, 1, 84, 1050L, 22, 100, "Automates stress-testing and collision detection."),
            Employee("c_${UUID.randomUUID().toString().take(4)}", "Lucas Silva", "Hype PR Strategist", "🧑‍💼", Department.SALES_MARKETING, 1, 87, 1250L, 25, 100, "Creates global buzz for upcoming product releases.")
        )
        return (existing + pool.shuffled().take(2)).distinctBy { it.name }
    }

    private fun updateAchievementsProgress(
        achievements: List<Achievement>,
        soloCount: Int = 0,
        capital: Long = 0,
        teamSize: Int = 0,
        cityTransform: Int = 0,
        sGrades: Int = 0,
        phoneDelivered: Int = 0,
        carDelivered: Int = 0,
        droneDelivered: Int = 0
    ): List<Achievement> {
        return achievements.map { ach ->
            val prog = when (ach.id) {
                "ach_solo_01" -> soloCount.coerceAtLeast(ach.currentProgress)
                "ach_solo_02" -> capital.toInt().coerceAtLeast(ach.currentProgress)
                "ach_staff_01" -> teamSize.coerceAtLeast(ach.currentProgress)
                "ach_staff_02" -> teamSize.coerceAtLeast(ach.currentProgress)
                "ach_prod_phone" -> phoneDelivered.coerceAtLeast(ach.currentProgress)
                "ach_prod_tesla" -> carDelivered.coerceAtLeast(ach.currentProgress)
                "ach_prod_spacex" -> droneDelivered.coerceAtLeast(ach.currentProgress)
                "ach_city_01" -> cityTransform.coerceAtLeast(ach.currentProgress)
                "ach_city_02" -> cityTransform.coerceAtLeast(ach.currentProgress)
                "ach_quality_s" -> sGrades.coerceAtLeast(ach.currentProgress)
                else -> ach.currentProgress
            }
            val unlocked = prog >= ach.targetProgress
            ach.copy(currentProgress = prog, isUnlocked = unlocked || ach.isUnlocked)
        }
    }
}
