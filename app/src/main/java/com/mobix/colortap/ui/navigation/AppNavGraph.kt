package com.mobix.colortap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobix.colortap.ui.screens.GameScreen
import com.mobix.colortap.ui.screens.HomeScreen
import com.mobix.colortap.ui.screens.ResultScreen

object Routes {
    const val HOME = "home"
    const val GAME = "game"
    const val RESULT = "result"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onStart = { navController.navigate(Routes.GAME) }
            )
        }
        composable(Routes.GAME) {
            GameScreen(
                onGameOver = { finalScore ->
                    navController.navigate("${Routes.RESULT}/$finalScore")
                }
            )
        }
        composable("${Routes.RESULT}/{score}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            ResultScreen(
                score = score,
                onPlayAgain = {
                    navController.navigate(Routes.GAME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onHome = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }
    }
}