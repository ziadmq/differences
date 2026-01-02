package com.mobix.differences.ui.screens.game.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import com.mobix.differences.data.model.CircleRegion
import com.mobix.differences.data.model.RectRegion
import com.mobix.differences.data.model.Region
import com.mobix.differences.ui.screens.game.TransformState
import kotlin.math.min

@Composable
fun DiffImage(
    imageRes: Int,
    regions: List<Region>,
    found: Set<String>,
    hintRegionId: String?,
    transform: TransformState,
    onTransformChange: (TransformState) -> Unit,
    onTapNormalized: (nx: Float, ny: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    val gestureModifier = Modifier.pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            val newScale = (transform.scale * zoom).coerceIn(1f, 4f)
            val newOffset = transform.offset + pan
            onTransformChange(transform.copy(scale = newScale, offset = newOffset))
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged { boxSize = it }
            .then(gestureModifier)
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    if (boxSize.width == 0 || boxSize.height == 0) return@detectTapGestures

                    // inverse transform: screen -> content
                    val cx = (tap.x - transform.offset.x) / transform.scale
                    val cy = (tap.y - transform.offset.y) / transform.scale

                    val nx = (cx / boxSize.width.toFloat()).coerceIn(0f, 1f)
                    val ny = (cy / boxSize.height.toFloat()).coerceIn(0f, 1f)

                    onTapNormalized(nx, ny)
                }
            }
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = transform.scale
                    scaleY = transform.scale
                    translationX = transform.offset.x
                    translationY = transform.offset.y
                }
        )

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = transform.scale
                    scaleY = transform.scale
                    translationX = transform.offset.x
                    translationY = transform.offset.y
                }
        ) {
            val w = size.width
            val h = size.height

            fun drawRegion(region: Region, alpha: Float) {
                when (region) {
                    is RectRegion -> drawRect(
                        color = Color.Green.copy(alpha = alpha),
                        topLeft = Offset(region.left * w, region.top * h),
                        size = Size((region.right - region.left) * w, (region.bottom - region.top) * h),
                        style = Stroke(width = 6f)
                    )
                    is CircleRegion -> drawCircle(
                        color = Color.Green.copy(alpha = alpha),
                        radius = region.r * min(w, h),
                        center = Offset(region.cx * w, region.cy * h),
                        style = Stroke(width = 6f)
                    )
                }
            }

            // Found regions
            regions.filter { it.id in found }.forEach { drawRegion(it, 0.9f) }

            // Hint region
            val hint = hintRegionId?.let { id -> regions.firstOrNull { it.id == id } }
            if (hint != null) drawRegion(hint, 0.6f)
        }
    }
}
