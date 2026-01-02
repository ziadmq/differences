package com.mobix.differences.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobix.differences.ui.screens.game.GameScreen
import com.mobix.differences.ui.screens.home.HomeScreen

@Composable
fun AppNavHost() {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Destinations.HOME
    ) {
        composable(Destinations.HOME) {
            HomeScreen(
                onStartLevel = { levelId ->
                    nav.navigate(Destinations.game(levelId))
                }
            )
        }

        composable(Destinations.GAME) { backStack ->
            val levelId = backStack.arguments?.getString("levelId") ?: "1"
            GameScreen(
                levelId = levelId,
                onBack = { nav.popBackStack() }
            )
        }
    }
}

