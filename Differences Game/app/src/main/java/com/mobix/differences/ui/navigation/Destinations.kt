package com.mobix.differences.ui.navigation

object Destinations {
    const val HOME = "home"
    const val GAME = "game/{levelId}"

    fun game(levelId: String): String = "game/$levelId"
}
