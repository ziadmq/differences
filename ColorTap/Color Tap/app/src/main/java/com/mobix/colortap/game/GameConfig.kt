package com.mobix.colortap.game

import androidx.compose.ui.graphics.Color

// All Enums defined here once to avoid redeclaration errors
enum class TargetType { NORMAL, TRAP, GOLDEN, POWER_UP }
enum class PowerUpType { NONE, FREEZE, SHIELD, MULTIPLIER }

data class LevelData(
    val levelNumber: Int,
    val targetScore: Int,
    val timeLimit: Int,
    val targetLifeMs: Long
)

object GameConfig {
    const val ROUND_SECONDS = 30
    const val FEVER_COMBO_THRESHOLD = 8
    const val SPECIAL_TARGET_CHANCE = 0.10f
    const val TRAP_CHANCE = 0.12f

    val LEVELS = listOf(
        LevelData(1, 15, 30, 1000L),
        LevelData(2, 25, 25, 800L),
        LevelData(3, 40, 20, 650L),
        LevelData(4, 60, 20, 500L)
    )
}