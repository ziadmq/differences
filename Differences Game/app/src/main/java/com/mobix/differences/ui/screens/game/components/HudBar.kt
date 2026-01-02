package com.mobix.differences.ui.screens.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HudBar(
    timeLeftSec: Int,
    mistakes: Int,
    maxMistakes: Int,
    found: Int,
    total: Int
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("الوقت: ${timeLeftSec}s")
        Text("الأخطاء: $mistakes/$maxMistakes")
        Text("المكتشف: $found/$total")
    }
}
