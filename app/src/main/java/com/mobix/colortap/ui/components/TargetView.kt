package com.mobix.colortap.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TargetView(color: Color, onTap: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse), label = ""
    )

    Box(
        modifier = Modifier
            .size(75.dp)
            .scale(scale)
            .shadow(15.dp, CircleShape, spotColor = color)
            .background(Brush.radialGradient(listOf(color, color.copy(0.6f), color.copy(0.2f))), CircleShape)
            .border(2.dp, Color.White.copy(0.5f), CircleShape)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onTap() }
    )
}