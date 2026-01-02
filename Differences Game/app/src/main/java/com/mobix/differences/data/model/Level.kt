package com.mobix.differences.data.model


data class Level(
    val id: String,
    val title: String,
    val imageARes: Int,
    val imageBRes: Int,
    val regions: List<Region>,
    val timeLimitSec: Int = 90,
    val maxMistakes: Int = 5
)
