package com.mobix.differences.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobix.differences.data.repo.LevelsRepository
import com.mobix.differences.ui.screens.game.components.DiffImage
import com.mobix.differences.ui.screens.game.components.HudBar

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

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        HudBar(
            timeLeftSec = state.timeLeftSec,
            mistakes = state.mistakes,
            maxMistakes = level.maxMistakes,
            found = state.found.size,
            total = level.regions.size
        )

        Spacer(Modifier.height(10.dp))

        Row(Modifier.weight(1f)) {
            DiffImage(
                imageRes = level.imageARes,
                regions = level.regions,
                found = state.found,
                hintRegionId = state.hintRegionId,
                transform = transform,
                onTransformChange = { transform = it },
                onTapNormalized = { nx, ny -> vm.onTap(level, nx, ny) },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(end = 6.dp)
            )
            DiffImage(
                imageRes = level.imageBRes,
                regions = level.regions,
                found = state.found,
                hintRegionId = state.hintRegionId,
                transform = transform,
                onTransformChange = { transform = it },
                onTapNormalized = { nx, ny -> vm.onTap(level, nx, ny) },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(start = 6.dp)
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onBack) { Text("رجوع") }

            Button(onClick = { vm.hint(level) }, enabled = !state.isFinished) {
                Text("تلميح")
            }

            Button(onClick = { transform = TransformState() }) {
                Text("Reset Zoom")
            }
        }

        if (state.isFinished) {
            Spacer(Modifier.height(10.dp))
            val win = state.found.size == level.regions.size
            Text(
                if (win) "🎉 أحسنت! اكتملت المرحلة."
                else "انتهت المحاولة! حاول مرة أخرى."
            )
        }
    }
}
