package com.mobix.differences.data.repo

import com.mobix.differences.data.model.*
import com.mobix.differences.R

object LevelsRepository {

    private val levels = listOf(
        Level(
            id = "1",
            title = "المرحلة الأولى: الغابة",
            type = LevelType.SPOT_DIFFERENCES,
            difficulty = Difficulty.EASY,
            imageARes = R.drawable.ic_launcher_background, // استبدلها بصورك
            imageBRes = R.drawable.ic_launcher_background,
            regions = listOf(
                RectRegion(id = "r1", left = 0.12f, top = 0.20f, right = 0.20f, bottom = 0.30f),
                CircleRegion(id = "c1", cx = 0.70f, cy = 0.45f, r = 0.06f),
            ),
            timeLimitSec = 60
        ),
        Level(
            id = "2",
            title = "المرحلة الثانية: الفضاء",
            type = LevelType.FIND_THE_ODD,
            difficulty = Difficulty.MEDIUM,
            imageARes = R.drawable.ic_launcher_background,
            regions = listOf(
                CircleRegion(id = "odd1", cx = 0.5f, cy = 0.5f, r = 0.1f)
            ),
            description = "رائد الفضاء يحمل مظلة شمسية في الفضاء! هذا غير منطقي.",
            timeLimitSec = 45
        )
    )

    fun getAllLevels(): List<Level> = levels
    fun getLevel(id: String): Level = levels.first { it.id == id }
}