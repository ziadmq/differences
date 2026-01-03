package com.mobix.colortap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobix.colortap.ui.screens.*

object Routes {
    const val SPLASH = "splash" // المسار الجديد لشاشة البداية
    const val HOME = "home"
    const val GAME = "game"
    const val RESULT = "result"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    // قمنا بتغيير startDestination لتكون شاشة البداية
    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        // 1. شاشة البداية (تظهر أولاً لمدة ثانيتين)
        composable(Routes.SPLASH) {
            SplashScreen(onAnimationFinished = {
                navController.navigate(Routes.HOME) {
                    // نقوم بحذف شاشة البداية من الـ BackStack حتى لا يعود إليها اللاعب
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }

        // 2. الشاشة الرئيسية
        composable(Routes.HOME) {
            HomeScreen(
                onStart = { navController.navigate(Routes.GAME) }
            )
        }

        // 3. شاشة اللعب الاحترافية
        composable(Routes.GAME) {
            GameScreen(
                onGameOver = { finalScore ->
                    navController.navigate("${Routes.RESULT}/$finalScore")
                }
            )
        }

        // 4. شاشة النتائج
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