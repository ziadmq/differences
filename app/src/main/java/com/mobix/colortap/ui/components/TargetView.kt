package com.mobix.colortap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TargetView(
    color: Color,
    size: Dp = 72.dp,
    onTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(10.dp, CircleShape)
            .background(color, CircleShape)
            .clickable { onTap() }
    )
}