package com.mobix.colortap.game

import androidx.compose.ui.graphics.Color

data class Particle(val id: Long, val x: Float, val y: Float, val vx: Float, val vy: Float, val color: Color, val alpha: Float = 1f)
data class TapEffect(val id: Long, val x: Float, val y: Float, val text: String, val color: Color)

data class GameState(
    val isRunning: Boolean = false,
    val isGameOver: Boolean = false,
    val currentLevel: Int = 1,
    val score: Int = 0,
    val scoreInLevel: Int = 0,
    val bestScore: Int = 0,
    val timeLeft: Int = 30,
    val combo: Int = 0,
    val isFeverMode: Boolean = false,

    // Mission and Difficulty features
    val missionDescription: String = "Tap 10 targets",
    val missionProgress: Int = 0,
    val missionGoal: Int = 10,
    val isTimeFrozen: Boolean = false,

    val targetVisible: Boolean = false,
    val targetX: Float = 0f,
    val targetY: Float = 0f,
    val targetColorArgb: Long = 0xFF4CAF50,
    val targetType: TargetType = TargetType.NORMAL,
    val powerUpType: PowerUpType = PowerUpType.NONE,

    val particles: List<Particle> = emptyList(),
    val effects: List<TapEffect> = emptyList(),
    val showLevelUp: Boolean = false,
    val areaWidthPx: Int = 0,
    val areaHeightPx: Int = 0,
    val isScreenShaking: Boolean = false
) {
    val levelProgress: Float get() = scoreInLevel.toFloat() / (GameConfig.LEVELS.getOrNull(currentLevel - 1)?.targetScore ?: 1)
}