package com.mobix.colortap.game

data class GameState(
    val isRunning: Boolean = false,
    val isGameOver: Boolean = false,

    val score: Int = 0,
    val bestScore: Int = 0,
    val timeLeft: Int = GameConfig.ROUND_SECONDS,

    // Target info (px)
    val targetVisible: Boolean = false,
    val targetX: Float = 0f,
    val targetY: Float = 0f,
    val targetColorArgb: Long = 0xFF4CAF50, // default

    val combo: Int = 0,
    val targetLifeMs: Long = GameConfig.START_LIFE_MS,

    // Play area size (px)
    val areaWidthPx: Int = 0,
    val areaHeightPx: Int = 0,

    // Used to trigger new targets
    val targetId: Long = 0L
)