package com.mobix.colortap.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopHud(
    timeLeft: Int,
    score: Int,
    combo: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "⏱️ ${timeLeft}s",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "⭐ $score",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "🔥 $combo",
            style = MaterialTheme.typography.titleMedium
        )
    }
}