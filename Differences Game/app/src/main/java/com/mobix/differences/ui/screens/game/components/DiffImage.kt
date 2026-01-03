package com.mobix.differences.ui.screens.game.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mobix.differences.data.model.*
import com.mobix.differences.ui.theme.GlassEffect
import com.mobix.differences.ui.theme.NeonCyan
import com.mobix.differences.ui.theme.NeonPink
import kotlin.math.min

@Composable
fun DiffImage(
    imageRes: Int,
    regions: List<Region>,
    found: Set<String>,
    hintRegionId: String?,
    transform: com.mobix.differences.ui.screens.game.TransformState,
    onTapNormalized: (nx: Float, ny: Float) -> Unit, // أضفنا هذا السطر
    modifier: Modifier = Modifier
) {
    // تعريف متغير حجم الحاوية (هذا هو السطر الذي كان ينقصك)
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .border(2.dp, GlassEffect, RoundedCornerShape(24.dp))
            .onSizeChanged { boxSize = it }
            .pointerInput(transform) {
                detectTapGestures { tapOffset ->
                    if (boxSize.width == 0 || boxSize.height == 0) return@detectTapGestures

                    // تحويل إحداثيات اللمس بناءً على التكبير والإزاحة
                    val contentX = (tapOffset.x - transform.offset.x) / transform.scale
                    val contentY = (tapOffset.y - transform.offset.y) / transform.scale

                    // تحويل الإحداثيات إلى قيم نسبية (0..1)
                    val normalizedX = (contentX / boxSize.width.toFloat()).coerceIn(0f, 1f)
                    val normalizedY = (contentY / boxSize.height.toFloat()).coerceIn(0f, 1f)

                    onTapNormalized(normalizedX, normalizedY)
                }
            }
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = transform.scale
                    scaleY = transform.scale
                    translationX = transform.offset.x
                    translationY = transform.offset.y
                }
        )

        Canvas(Modifier.fillMaxSize().graphicsLayer {
            scaleX = transform.scale
            scaleY = transform.scale
            translationX = transform.offset.x
            translationY = transform.offset.y
        }) {
            val w = size.width
            val h = size.height

            // رسم الفروقات المكتشفة أو التلميح
            regions.forEach { region ->
                val isFound = region.id in found
                val isHint = region.id == hintRegionId

                if (isFound || isHint) {
                    val color = if (isHint) NeonPink else NeonCyan
                    when (region) {
                        is CircleRegion -> {
                            drawCircle(
                                color = color,
                                center = Offset(region.cx * w, region.cy * h),
                                radius = region.r * min(w, h),
                                style = Stroke(width = 8f)
                            )
                        }
                        is RectRegion -> {
                            drawRect(
                                color = color,
                                topLeft = Offset(region.left * w, region.top * h),
                                size = Size((region.right - region.left) * w, (region.bottom - region.top) * h),
                                style = Stroke(width = 8f)
                            )
                        }
                    }
                }
            }
        }
    }
}