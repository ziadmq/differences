package com.mobix.colortap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobix.colortap.game.GameConfig
import com.mobix.colortap.ui.components.TargetView
import com.mobix.colortap.ui.components.TopHud
import com.mobix.colortap.viewmodel.GameViewModel
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    onGameOver: (finalScore: Int) -> Unit,
    vm: GameViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    // Start game once when enter screen
    LaunchedEffect(Unit) {
        vm.startGame()
    }

    // Navigate when game over
    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) {
            onGameOver(state.score)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopHud(
            timeLeft = state.timeLeft,
            score = state.score,
            combo = state.combo
        )

        Spacer(Modifier.height(16.dp))

        // Play Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    vm.setPlayAreaSize(size.width, size.height)
                }
        ) {
            if (state.targetVisible) {
                val targetColor = Color(state.targetColorArgb)
                val sizeDp = GameConfig.TARGET_SIZE_DP.dp

                // offset uses px in IntOffset
                TargetView(
                    color = targetColor,
                    size = sizeDp,
                    onTap = { vm.onTargetTap() },
                ).let { target ->
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(state.targetX.roundToInt(), state.targetY.roundToInt())
                        }
                    ) { target }
                }
            }
        }
    }
}