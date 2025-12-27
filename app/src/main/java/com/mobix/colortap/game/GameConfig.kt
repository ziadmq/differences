package com.mobix.colortap.game

data class LevelData(
    val levelNumber: Int,
    val targetScore: Int,
    val timeLimit: Int,
    val targetLifeMs: Long
)

// أنواع القوى الخاصة
enum class PowerUpType { NONE, FREEZE, SHIELD, MULTIPLIER }

object GameConfig {
    const val ROUND_SECONDS = 30
    const val TARGET_SIZE_DP = 75
    const val FEVER_COMBO_THRESHOLD = 8
    const val SPECIAL_TARGET_CHANCE = 0.10f // فرصة ظهور Power-up
    const val TRAP_CHANCE = 0.12f

    val LEVELS = listOf(
        LevelData(1, 15, 30, 1000L),
        LevelData(2, 25, 25, 800L),
        LevelData(3, 40, 20, 650L),
        LevelData(4, 60, 20, 500L)
    )
}