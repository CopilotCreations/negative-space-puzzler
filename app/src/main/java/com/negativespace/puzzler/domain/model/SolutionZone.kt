package com.negativespace.puzzler.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a solution zone in the puzzle - an area the player must NOT touch.
 */
@Serializable
data class SolutionZone(
    val centerX: Float,
    val centerY: Float,
    val width: Float,
    val height: Float,
    val shape: ZoneShape = ZoneShape.RECTANGLE
) {
    init {
        require(centerX in 0f..1f) { "Center X must be normalized (0-1)" }
        require(centerY in 0f..1f) { "Center Y must be normalized (0-1)" }
        require(width > 0f && width <= 1f) { "Width must be positive and <= 1" }
        require(height > 0f && height <= 1f) { "Height must be positive and <= 1" }
    }
    
    val left: Float get() = (centerX - width / 2).coerceAtLeast(0f)
    val right: Float get() = (centerX + width / 2).coerceAtMost(1f)
    val top: Float get() = (centerY - height / 2).coerceAtLeast(0f)
    val bottom: Float get() = (centerY + height / 2).coerceAtMost(1f)
    
    /**
     * Checks if a point is inside the solution zone.
     */
    fun containsPoint(x: Float, y: Float): Boolean {
        return when (shape) {
            ZoneShape.RECTANGLE -> x in left..right && y in top..bottom
            ZoneShape.ELLIPSE -> {
                val dx = (x - centerX) / (width / 2)
                val dy = (y - centerY) / (height / 2)
                dx * dx + dy * dy <= 1f
            }
        }
    }
    
    /**
     * Checks if a touch point is inside the solution zone.
     */
    fun containsTouch(point: TouchPoint): Boolean {
        return containsPoint(point.x, point.y)
    }
    
    /**
     * Gets the grid cells that this zone covers.
     */
    fun getCoveredCells(gridWidth: Int, gridHeight: Int): List<Pair<Int, Int>> {
        val cells = mutableListOf<Pair<Int, Int>>()
        val startX = (left * gridWidth).toInt()
        val endX = (right * gridWidth).toInt().coerceAtMost(gridWidth - 1)
        val startY = (top * gridHeight).toInt()
        val endY = (bottom * gridHeight).toInt().coerceAtMost(gridHeight - 1)
        
        for (y in startY..endY) {
            for (x in startX..endX) {
                val normalizedX = (x + 0.5f) / gridWidth
                val normalizedY = (y + 0.5f) / gridHeight
                if (containsPoint(normalizedX, normalizedY)) {
                    cells.add(Pair(x, y))
                }
            }
        }
        
        return cells
    }
}

@Serializable
enum class ZoneShape {
    RECTANGLE,
    ELLIPSE
}
