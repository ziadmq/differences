package com.mobix.differences.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobix.differences.data.repo.LevelsRepository
import com.mobix.differences.ui.screens.game.components.*
import com.mobix.differences.ui.theme.*

@Composable
fun GameScreen(
    levelId: String,
    onBack: () -> Unit,
    vm: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val level = remember(levelId) { LevelsRepository.getLevel(levelId) }
    val state by vm.state.collectAsState()
    var transform by remember { mutableStateOf(TransformState()) }

    LaunchedEffect(level.id) { vm.start(level) }

    Box(Modifier.fillMaxSize().background(BgDark)) {
        // تدرج لوني خفيف في الخلفية لإعطاء عمق
        Box(Modifier.fillMaxSize().background(Brush.radialGradient(listOf(SurfaceDark, BgDark))))

        Column(Modifier.fillMaxSize().padding(16.dp)) {
            HudBar(state.timeLeftSec, state.hearts, state.found.size, level.regions.size)

            Spacer(Modifier.height(16.dp))

            // حاوية الصور - Focus Mode
            Column(Modifier.weight(1f)) {
                DiffImage(
                    imageRes = level.imageARes,
                    regions = level.regions,
                    found = state.found,
                    hintRegionId = state.hintRegionId,
                    transform = transform,
                    onTapNormalized = { nx, ny -> vm.onTap(level, nx, ny) }, // تمرير منطق النقر هنا
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                DiffImage(
                    imageRes = level.imageBRes ?: level.imageARes,
                    regions = level.regions,
                    found = state.found,
                    hintRegionId = state.hintRegionId,
                    transform = transform,
                    onTapNormalized = { nx, ny -> vm.onTap(level, nx, ny) }, // تمرير منطق النقر هنا
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            }

            Spacer(Modifier.height(20.dp))

            // شريط التحكم السفلي
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ControlBtn("🔙", onBack)
                ControlBtn("💡", { vm.hint(level) }, NeonPink)
                ControlBtn("🔄", { transform = TransformState() }, NeonCyan)
            }
        }

        // نافذة النتيجة عند انتهاء اللعبة
        if (state.isFinished) {
            GameResultOverlay(
                win = state.found.size == level.regions.size,
                onBack = onBack
            )
        }
    }
}

@Composable
fun ControlBtn(icon: String, onClick: () -> Unit, accent: Color = Color.White) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)
            .background(GlassEffect, RoundedCornerShape(18.dp))
            .border(1.dp, accent.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
    ) {
        Text(icon, fontSize = 24.sp)
    }
}

@Composable
fun GameResultOverlay(win: Boolean, onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (win) "LEVEL CLEAR!" else "GAME OVER",
                color = if (win) NeonCyan else NeonPink,
                fontSize = 44.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(56.dp).padding(horizontal = 32.dp)
            ) {
                Text("CONTINUE", color = BgDark, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
        }
    }
}