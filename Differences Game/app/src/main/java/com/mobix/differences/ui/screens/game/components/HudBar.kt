package com.mobix.differences.ui.screens.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobix.differences.ui.theme.*

@Composable
fun HudBar(timeLeftSec: Int, hearts: Int, found: Int, total: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clip(CircleShape)
            .background(GlassEffect)
            .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Timer with Glow logic
        StatPill("⏳", "%02d:%02d".format(timeLeftSec / 60, timeLeftSec % 60), if(timeLeftSec < 10) NeonPink else NeonCyan)

        // Hearts
        Row {
            repeat(5) { i ->
                Text(
                    text = if (i < hearts) "❤️" else "🖤",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 1.dp)
                )
            }
        }

        // Progress
        StatPill("🎯", "$found/$total", GoldYellow)
    }
}

@Composable
fun StatPill(icon: String, value: String, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 16.sp)
        Spacer(Modifier.width(4.dp))
        Text(value, color = accent, fontWeight = FontWeight.Black, fontSize = 16.sp)
    }
}