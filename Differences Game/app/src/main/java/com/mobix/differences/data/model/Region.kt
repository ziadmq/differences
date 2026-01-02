package com.mobix.differences.data.model


sealed interface Region {
    val id: String
    fun contains(nx: Float, ny: Float): Boolean // normalized 0..1
}

data class RectRegion(
    override val id: String,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) : Region {
    override fun contains(nx: Float, ny: Float): Boolean =
        nx in left..right && ny in top..bottom
}

data class CircleRegion(
    override val id: String,
    val cx: Float,
    val cy: Float,
    val r: Float
) : Region {
    override fun contains(nx: Float, ny: Float): Boolean {
        val dx = nx - cx
        val dy = ny - cy
        return (dx * dx + dy * dy) <= (r * r)
    }
}
