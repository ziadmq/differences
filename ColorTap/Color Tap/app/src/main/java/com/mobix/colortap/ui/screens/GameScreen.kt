package com.mobix.colortap.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobix.colortap.game.PowerUpType
import com.mobix.colortap.game.TapEffect
import com.mobix.colortap.game.TargetType
import com.mobix.colortap.ui.components.TargetView
import com.mobix.colortap.ui.components.TopHud
import com.mobix.colortap.ui.theme.BackgroundDark
import com.mobix.colortap.viewmodel.GameViewModel
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    onGameOver: (Int) -> Unit,
    vm: GameViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val haptic = LocalHapticFeedback.current

    val shakeOffset by animateDpAsState(
        targetValue = if (state.isScreenShaking) 15.dp else 0.dp,
        label = "shakeOffset"
    )

    val feverAlpha by animateFloatAsState(
        targetValue = if (state.isFeverMode) 0.25f else 0f,
        label = "feverAlpha"
    )

    LaunchedEffect(Unit) { vm.startGame() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Fever Mode Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Magenta.copy(alpha = feverAlpha)
                        )
                    )
                )
        )

        // Freeze overlay
        if (state.isTimeFrozen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan.copy(alpha = 0.2f))
                    .blur(10.dp)
            )
        }

        // Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            state.particles.forEach { p ->
                drawCircle(
                    color = p.color.copy(alpha = p.alpha),
                    radius = 8f,
                    center = Offset(p.x, p.y)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .offset(x = shakeOffset)
        ) {
            TopHud(state.timeLeft, state.score, state.combo)

            // Mission Tracker
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Column(Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "CURRENT MISSION",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.missionDescription,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${state.missionProgress}/${state.missionGoal}",
                            color = Color.Cyan,
                            fontWeight = FontWeight.Black
                        )
                    }

                    LinearProgressIndicator(
                        progress = { state.missionProgress.toFloat() / state.missionGoal.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(4.dp)
                            .clip(CircleShape),
                        color = Color.Cyan,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .onSizeChanged { vm.setPlayAreaSize(it.width, it.height) }
            ) {
                // Target
                if (state.targetVisible) {
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(
                                state.targetX.roundToInt(),
                                state.targetY.roundToInt()
                            )
                        }
                    ) {
                        TargetView(
                            color = Color(state.targetColorArgb),
                            onTap = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                vm.onTargetTap(state.targetX, state.targetY)
                            }
                        )

                        if (state.targetType == TargetType.POWER_UP) {
                            Text(
                                text = if (state.powerUpType == PowerUpType.FREEZE) "❄️" else "⚡",
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 24.sp
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {

                    androidx.compose.animation.AnimatedVisibility(
                        visible = state.showLevelUp,
                        modifier = Modifier.align(Alignment.Center),
                        enter = fadeIn() + scaleIn(initialScale = 0.8f),
                        exit = fadeOut() + scaleOut(targetScale = 1.2f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("MISSION COMPLETE!", color = Color.Cyan, fontWeight = FontWeight.Bold)
                            Text(
                                "LEVEL ${state.currentLevel + 1}",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Yellow
                            )
                        }
                    }
                }


                // Effects
                state.effects.forEach { FloatingTextEffect(it) }
            }
        }
    }

    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) onGameOver(state.score)
    }
}

@Composable
fun FloatingTextEffect(effect: TapEffect) {
    val animY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animY.animateTo(
            targetValue = -150f,
            animationSpec = tween(800)
        )
    }

    Text(
        text = effect.text,
        color = effect.color,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier
            .offset {
                IntOffset(
                    effect.x.roundToInt(),
                    (effect.y + animY.value).roundToInt()
                )
            }
            .alpha(1f - (animY.value / -150f))
    )
}
