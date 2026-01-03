package com.mobix.colortap.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// TopHud.kt
@Composable
fun TopHud(timeLeft: Int, score: Int, combo: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        HudCard("TIME", "${timeLeft}s", Color(0xFFFF3B30), Modifier.weight(1f))
        HudCard("SCORE", "$score", Color(0xFF34C759), Modifier.weight(1f))
        HudCard("COMBO", "x$combo", Color(0xFFFFCC00), Modifier.weight(1f))
    }
}
@Composable
fun HudCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(Color.White.copy(0.05f))
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(12.dp)).padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        AnimatedContent(targetState = value, label = "") { Text(it, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color) }
    }
}