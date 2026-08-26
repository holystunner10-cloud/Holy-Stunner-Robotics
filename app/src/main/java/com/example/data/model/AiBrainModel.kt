package com.example.data.model

data class NeuralModule(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val level: Int = 1,
    val maxLevel: Int = 5,
    val computeCost: Int,
    val efficiencyBonus: Float,
    val iconName: String
)

data class AiBrainConfig(
    val firmwareVersion: String = "Stunner-OS v5.0",
    val visionLiDAR: Int = 3,         // 1 to 5
    val naturalLanguage: Int = 3,     // 1 to 5
    val pathfindingAI: Int = 3,       // 1 to 5
    val fineMotorControl: Int = 3,    // 1 to 5
    val ethicsSafetyMatrix: Int = 4,  // 1 to 5
    val energyKernel: Int = 3,        // 1 to 5
    val activePerks: List<String> = listOf("Adaptive Learning Kernel")
) {
    val overallIntelligenceRating: Int
        get() = ((visionLiDAR + naturalLanguage + pathfindingAI + fineMotorControl + ethicsSafetyMatrix + energyKernel) * 100) / 30

    val safetyIndex: Int
        get() = ethicsSafetyMatrix * 20

    val efficiencyMultiplier: Float
        get() = 1f + (energyKernel * 0.05f)

    companion object {
        fun defaultFor(category: RobotCategory): AiBrainConfig {
            return when (category) {
                RobotCategory.CHEF_DOMESTIC -> AiBrainConfig(
                    firmwareVersion = "GourmetSynapse 3.0",
                    visionLiDAR = 4,
                    naturalLanguage = 3,
                    pathfindingAI = 2,
                    fineMotorControl = 5,
                    ethicsSafetyMatrix = 4,
                    energyKernel = 3,
                    activePerks = listOf("Michelin Recipe DB", "Zero-Spill Grip")
                )
                RobotCategory.TITAN_HUMANOID -> AiBrainConfig(
                    firmwareVersion = "TitanBiped-OS",
                    visionLiDAR = 5,
                    naturalLanguage = 4,
                    pathfindingAI = 4,
                    fineMotorControl = 5,
                    ethicsSafetyMatrix = 5,
                    energyKernel = 4,
                    activePerks = listOf("Bipedal Dynamic Balance", "Home Guard Security")
                )
                RobotCategory.MEDICAL_CARE -> AiBrainConfig(
                    firmwareVersion = "VitalisCare-v5",
                    visionLiDAR = 3,
                    naturalLanguage = 5,
                    pathfindingAI = 3,
                    fineMotorControl = 4,
                    ethicsSafetyMatrix = 5,
                    energyKernel = 3,
                    activePerks = listOf("Turing Empathy Core", "Vitals Telemetry")
                )
                RobotCategory.CONSTRUCTION_UTILITY -> AiBrainConfig(
                    firmwareVersion = "TitanKernel 4",
                    visionLiDAR = 4,
                    naturalLanguage = 1,
                    pathfindingAI = 3,
                    fineMotorControl = 5,
                    ethicsSafetyMatrix = 4,
                    energyKernel = 5,
                    activePerks = listOf("Heavy Torque Loadout", "Solar Grid Sync")
                )
                RobotCategory.AI_SMARTPHONE -> AiBrainConfig(
                    firmwareVersion = "NeuralPhone-OS 1.0",
                    visionLiDAR = 4,
                    naturalLanguage = 5,
                    pathfindingAI = 3,
                    fineMotorControl = 2,
                    ethicsSafetyMatrix = 4,
                    energyKernel = 5,
                    activePerks = listOf("Offline Neural Co-Pilot", "Quantum Encryption")
                )
                RobotCategory.AI_FOLDABLE_PHONE -> AiBrainConfig(
                    firmwareVersion = "FoldQuantum-OS",
                    visionLiDAR = 4,
                    naturalLanguage = 5,
                    pathfindingAI = 3,
                    fineMotorControl = 2,
                    ethicsSafetyMatrix = 4,
                    energyKernel = 5,
                    activePerks = listOf("Satellite Mesh Relay", "Dual-Screen Neural Split")
                )
                RobotCategory.AI_AUTO_CAR -> AiBrainConfig(
                    firmwareVersion = "HyperDrive-L5",
                    visionLiDAR = 5,
                    naturalLanguage = 2,
                    pathfindingAI = 5,
                    fineMotorControl = 3,
                    ethicsSafetyMatrix = 5,
                    energyKernel = 4,
                    activePerks = listOf("Mesh Fleet Radar", "Pedestrian Shield")
                )
                RobotCategory.AI_CYBER_TRUCK -> AiBrainConfig(
                    firmwareVersion = "CyberTough-OS",
                    visionLiDAR = 5,
                    naturalLanguage = 2,
                    pathfindingAI = 5,
                    fineMotorControl = 4,
                    ethicsSafetyMatrix = 5,
                    energyKernel = 5,
                    activePerks = listOf("Off-Grid Convoy Sync", "Armored Collision Matrix")
                )
                RobotCategory.AI_SMART_BIKE -> AiBrainConfig(
                    firmwareVersion = "PulseGyro-OS",
                    visionLiDAR = 4,
                    naturalLanguage = 2,
                    pathfindingAI = 4,
                    fineMotorControl = 4,
                    ethicsSafetyMatrix = 4,
                    energyKernel = 4,
                    activePerks = listOf("Alleyway Nav Matrix", "Kinetic Auto-Balance")
                )
                RobotCategory.AI_SMART_TV -> AiBrainConfig(
                    firmwareVersion = "VisionHolo-OS",
                    visionLiDAR = 4,
                    naturalLanguage = 5,
                    pathfindingAI = 2,
                    fineMotorControl = 2,
                    ethicsSafetyMatrix = 4,
                    energyKernel = 4,
                    activePerks = listOf("Spatial Hologram Engine", "Smart City Hub Broadcast")
                )
                RobotCategory.DELIVERY_DRONE -> AiBrainConfig(
                    firmwareVersion = "AeroDrop-Nav 2",
                    visionLiDAR = 5,
                    naturalLanguage = 1,
                    pathfindingAI = 5,
                    fineMotorControl = 3,
                    ethicsSafetyMatrix = 4,
                    energyKernel = 4,
                    activePerks = listOf("Wind Shear Compensator", "Doorstep Sensor")
                )
                RobotCategory.FALCON_ORBITAL_DRONE -> AiBrainConfig(
                    firmwareVersion = "StarLift-Orbital v9",
                    visionLiDAR = 5,
                    naturalLanguage = 2,
                    pathfindingAI = 5,
                    fineMotorControl = 4,
                    ethicsSafetyMatrix = 5,
                    energyKernel = 5,
                    activePerks = listOf("Supersonic VTOL Engine", "Thermal Re-entry Shield")
                )
            }
        }
    }
}
