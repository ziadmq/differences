package com.mobix.differences.data.repo


import com.mobix.differences.data.model.CircleRegion
import com.mobix.differences.data.model.Level
import com.mobix.differences.data.model.RectRegion
import com.mobix.differences.R

object LevelsRepository {

    private val levels = listOf(
        Level(
            id = "1",
            title = "مرحلة 1",
            imageARes = R.drawable.ic_launcher_background,
            imageBRes = R.drawable.ic_launcher_background,
            regions = listOf(
                RectRegion(id = "r1", left = 0.12f, top = 0.20f, right = 0.20f, bottom = 0.30f),
                CircleRegion(id = "c1", cx = 0.70f, cy = 0.45f, r = 0.06f),
                RectRegion(id = "r2", left = 0.40f, top = 0.70f, right = 0.55f, bottom = 0.85f),
            ),
            timeLimitSec = 90,
            maxMistakes = 5
        )
    )

    fun getAllLevels(): List<Level> = levels
    fun getLevel(id: String): Level = levels.first { it.id == id }
}
