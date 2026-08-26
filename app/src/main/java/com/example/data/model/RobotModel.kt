package com.example.data.model

enum class ProductCategoryGroup(val displayName: String, val iconName: String) {
    ROBOTICS("Robotics & Androids", "SmartToy"),
    MOBILE_DEVICES("Smartphones & Neural Tech", "Smartphone"),
    VEHICLES("Autonomous Vehicles & Bikes", "DirectionsCar"),
    HOME_ENTERTAINMENT("Smart TVs & Living", "Tv"),
    AEROSPACE_DRONES("Drones & Aerospace", "Flight")
}

enum class RobotCategory(
    val title: String,
    val description: String,
    val iconName: String,
    val baseCost: Long,
    val basePrice: Long,
    val buildTimeSec: Int,
    val cityImpactType: String,
    val group: ProductCategoryGroup
) {
    // 1. Robotics
    CHEF_DOMESTIC(
        title = "ChefBot Omni-9",
        description = "Precision culinary robot with multi-arm spatula, Michelin recipe matrix & kitchen cleaning.",
        iconName = "Restaurant",
        baseCost = 4500,
        basePrice = 12500,
        buildTimeSec = 6,
        cityImpactType = "Domestic & Gourmet Cuisine",
        group = ProductCategoryGroup.ROBOTICS
    ),
    TITAN_HUMANOID(
        title = "Optimus Titan Android",
        description = "Full-body bipedal humanoid assistant for home chores, elder assistance & corporate security.",
        iconName = "SmartToy",
        baseCost = 15000,
        basePrice = 38000,
        buildTimeSec = 10,
        cityImpactType = "Household & Security",
        group = ProductCategoryGroup.ROBOTICS
    ),
    MEDICAL_CARE(
        title = "VitalCare Medi-Bot",
        description = "Compassionate companion robot with medical diagnostics, vital monitoring & emergency readiness.",
        iconName = "HealthAndSafety",
        baseCost = 8500,
        basePrice = 22000,
        buildTimeSec = 8,
        cityImpactType = "Healthcare & Companionship",
        group = ProductCategoryGroup.ROBOTICS
    ),
    CONSTRUCTION_UTILITY(
        title = "TitanBuilder M4",
        description = "Heavy-duty smart robot for solar installations, smart grid repairs & green architecture.",
        iconName = "Engineering",
        baseCost = 18000,
        basePrice = 45000,
        buildTimeSec = 12,
        cityImpactType = "Infrastructure & Clean Grid",
        group = ProductCategoryGroup.ROBOTICS
    ),

    // 2. Mobile Neural Devices (Elon / Steve Jobs style smartphone innovation)
    AI_SMARTPHONE(
        title = "Stunner CyberPhone Pro",
        description = "Edge-AI neural smartphone with offline holographic assistant, zero-latency translation & quantum chip.",
        iconName = "Smartphone",
        baseCost = 1200,
        basePrice = 3400,
        buildTimeSec = 4,
        cityImpactType = "Mobile AI & Connectivity",
        group = ProductCategoryGroup.MOBILE_DEVICES
    ),
    AI_FOLDABLE_PHONE(
        title = "Stunner Fold Quantum",
        description = "Dual-display flexible graphene smartphone with satellite mesh AI and neural brain sync.",
        iconName = "DevicesFold",
        baseCost = 2200,
        basePrice = 5800,
        buildTimeSec = 5,
        cityImpactType = "Quantum Communication",
        group = ProductCategoryGroup.MOBILE_DEVICES
    ),

    // 3. Autonomous Vehicles & Bikes (Tesla / CyberTruck / Smart Bike)
    AI_AUTO_CAR(
        title = "Stunner HyperDrive Sedan",
        description = "Level-5 fully autonomous electric AI car. Zero traffic collisions, regenerative solar roof.",
        iconName = "DirectionsCar",
        baseCost = 35000,
        basePrice = 85000,
        buildTimeSec = 15,
        cityImpactType = "Autonomous Green Transit",
        group = ProductCategoryGroup.VEHICLES
    ),
    AI_CYBER_TRUCK(
        title = "Stunner CyberTruck Exoskeleton",
        description = "Indestructible cold-rolled steel electric truck with automated towing convoy & disaster relief AI.",
        iconName = "LocalShipping",
        baseCost = 48000,
        basePrice = 115000,
        buildTimeSec = 18,
        cityImpactType = "Heavy Transport & Logistics",
        group = ProductCategoryGroup.VEHICLES
    ),
    AI_SMART_BIKE(
        title = "Stunner Pulse CyberBike",
        description = "Self-balancing gyroscopic AI commuter bike with intelligent collision shield & auto-pedal assist.",
        iconName = "TwoWheeler",
        baseCost = 1800,
        basePrice = 4900,
        buildTimeSec = 5,
        cityImpactType = "Eco Urban Mobility",
        group = ProductCategoryGroup.VEHICLES
    ),

    // 4. Smart TVs & Living Room Hubs
    AI_SMART_TV(
        title = "Stunner Vision 8K HoloTV",
        description = "100-inch borderless neural OLED screen with spatial AI hologram projection & smart city home hub.",
        iconName = "Tv",
        baseCost = 2800,
        basePrice = 7200,
        buildTimeSec = 6,
        cityImpactType = "Smart Home & Entertainment",
        group = ProductCategoryGroup.HOME_ENTERTAINMENT
    ),

    // 5. Drones & Aerospace (SpaceX style)
    DELIVERY_DRONE(
        title = "AeroDrop X1 Drone",
        description = "High-speed quadcopter with laser obstacle radar for instant rooftop & doorstep drops.",
        iconName = "Flight",
        baseCost = 3200,
        basePrice = 8600,
        buildTimeSec = 7,
        cityImpactType = "Sky Logistics & Delivery",
        group = ProductCategoryGroup.AEROSPACE_DRONES
    ),
    FALCON_ORBITAL_DRONE(
        title = "Falcon StarLift Heavy",
        description = "Hypersonic VTOL cargo rocket-drone capable of regional heavy container express & space cargo.",
        iconName = "RocketLaunch",
        baseCost = 75000,
        basePrice = 195000,
        buildTimeSec = 22,
        cityImpactType = "Aerospace & Sub-Orbital Freight",
        group = ProductCategoryGroup.AEROSPACE_DRONES
    )
}

enum class TestGrade(val gradeLetter: String, val multiplier: Float, val badgeColorHex: Long) {
    UNTESTED("Untested", 0.70f, 0xFF64748B),
    GRADE_C("C - Passable", 0.90f, 0xFFF59E0B),
    GRADE_B("B - Solid", 1.15f, 0xFF38BDF8),
    GRADE_A("A - Superior", 1.45f, 0xFF10B981),
    GRADE_S("S+ - Holy Stunner", 1.80f, 0xFFFFB703)
}

data class RobotUnit(
    val id: String,
    val serialNumber: String,
    val category: RobotCategory,
    val customName: String = category.title,
    val brainConfig: AiBrainConfig = AiBrainConfig.defaultFor(category),
    val testGrade: TestGrade = TestGrade.UNTESTED,
    val testScore: Int = 0,
    val isCompleted: Boolean = false,
    val isTested: Boolean = false,
    val isDelivered: Boolean = false,
    val buildProgress: Float = 0f, // 0f to 1f
    val productionCost: Long = category.baseCost,
    val qualityScore: Int = 80,
    val assignedDistrictId: String? = null
) {
    val finalSalePrice: Long
        get() = (category.basePrice * testGrade.multiplier).toLong()
}
