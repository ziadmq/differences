package com.mobix.colortap.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobix.colortap.game.*
import com.mobix.colortap.ui.components.*
import com.mobix.colortap.ui.theme.BackgroundDark
import com.mobix.colortap.viewmodel.GameViewModel
import kotlin.math.roundToInt

@Composable
fun GameScreen(onGameOver: (Int) -> Unit, vm: GameViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    val haptic = LocalHapticFeedback.current
    val shakeOffset by animateDpAsState(if (state.isScreenShaking) 15.dp else 0.dp)

    LaunchedEffect(Unit) { vm.startGame() }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        // تأثير الثلج عند تجميد الوقت
        if (state.isTimeFrozen) {
            Box(Modifier.fillMaxSize().background(Color.Cyan.copy(alpha = 0.2f)).blur(10.dp))
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            state.particles.forEach { p -> drawCircle(p.color.copy(alpha = p.alpha), 8f, Offset(p.x, p.y)) }
        }

        Column(modifier = Modifier.fillMaxSize().padding(20.dp).offset(x = shakeOffset)) {
            TopHud(state.timeLeft, state.score, state.combo)

            // لوحة المهمة (Mission UI)
            Surface(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("MISSION", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(state.missionDescription, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Text("${state.missionProgress}/${state.missionGoal}", color = Color.Cyan, fontWeight = FontWeight.Black)
                }
            }

            LinearProgressIndicator(
                progress = { state.levelProgress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = if (state.isFeverMode) Color.Magenta else Color.Cyan
            )

            Box(modifier = Modifier.fillMaxSize().weight(1f).onSizeChanged { vm.setPlayAreaSize(it.width, it.height) }) {
                if (state.targetVisible) {
                    Box(modifier = Modifier.offset { IntOffset(state.targetX.roundToInt(), state.targetY.roundToInt()) }) {
                        TargetView(color = Color(state.targetColorArgb), onTap = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            vm.onTargetTap(state.targetX, state.targetY)
                        })
                        if (state.targetType == TargetType.POWER_UP) Text("⚡", Modifier.align(Alignment.Center), fontSize = 24.sp)
                    }
                }

                this@Column.AnimatedVisibility(visible = state.showLevelUp, modifier = Modifier.align(Alignment.Center)) {
                    Text("LEVEL UP!", fontSize = 45.sp, fontWeight = FontWeight.Black, color = Color.Yellow)
                }
                state.effects.forEach { FloatingTextEffect(it) }
            }
        }
    }
    LaunchedEffect(state.isGameOver) { if (state.isGameOver) onGameOver(state.score) }
}

@Composable
fun FloatingTextEffect(effect: TapEffect) {
    val animY = remember { Animatable(0f) }
    LaunchedEffect(Unit) { animY.animateTo(-150f, tween(800)) }
    Text(effect.text, color = effect.color, fontSize = 20.sp, fontWeight = FontWeight.Black,
        modifier = Modifier.offset { IntOffset(effect.x.roundToInt(), (effect.y + animY.value).roundToInt()) }.alpha(1f - (animY.value / -150f)))
}