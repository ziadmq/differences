package com.mobix.differences.data.model

enum class LevelType {
    SPOT_DIFFERENCES, // صورتان
    FIND_THE_ODD      // صورة واحدة لشيء غير منطقي
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}

data class Level(
    val id: String,
    val title: String,
    val type: LevelType = LevelType.SPOT_DIFFERENCES,
    val difficulty: Difficulty = Difficulty.EASY,
    val imageARes: Int,
    val imageBRes: Int? = null, // قد يكون نول في حال كان النمط "شيء غير منطقي"
    val regions: List<Region>,
    val timeLimitSec: Int = 90,
    val maxMistakes: Int = 5,
    val description: String = "" // شرح للحل في نمط الشيء غير المنطقي
)