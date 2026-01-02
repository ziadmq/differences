package com.mobix.differences.data.model

data class GameResult(
    val levelId: String,
    val win: Boolean,
    val timeSpentSec: Int,
    val mistakes: Int,
    val foundCount: Int,
    val totalCount: Int
)
