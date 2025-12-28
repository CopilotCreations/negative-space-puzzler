package com.negativespace.puzzler.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a heatmap of touch interactions on the canvas.
 * Uses a grid-based approach for efficient storage and rendering.
 */
@Serializable
data class Heatmap(
    val gridWidth: Int = DEFAULT_GRID_SIZE,
    val gridHeight: Int = DEFAULT_GRID_SIZE,
    val cells: List<Float> = List(gridWidth * gridHeight) { 0f },
    val maxIntensity: Float = 1f
) {
    init {
        require(gridWidth > 0) { "Grid width must be positive" }
        require(gridHeight > 0) { "Grid height must be positive" }
        require(cells.size == gridWidth * gridHeight) { "Cells size must match grid dimensions" }
    }
    
    /**
     * Gets the intensity value at a specific grid cell.
     */
    fun getIntensity(gridX: Int, gridY: Int): Float {
        if (gridX !in 0 until gridWidth || gridY !in 0 until gridHeight) {
            return 0f
        }
        return cells[gridY * gridWidth + gridX]
    }
    
    /**
     * Creates a new heatmap with a touch point added.
     */
    fun addTouch(point: TouchPoint, radius: Int = DEFAULT_TOUCH_RADIUS): Heatmap {
        val newCells = cells.toMutableList()
        val centerX = (point.x * gridWidth).toInt().coerceIn(0, gridWidth - 1)
        val centerY = (point.y * gridHeight).toInt().coerceIn(0, gridHeight - 1)
        
        for (dy in -radius..radius) {
            for (dx in -radius..radius) {
                val gx = centerX + dx
                val gy = centerY + dy
                if (gx in 0 until gridWidth && gy in 0 until gridHeight) {
                    val distance = kotlin.math.sqrt((dx * dx + dy * dy).toFloat())
                    if (distance <= radius) {
                        val falloff = 1f - (distance / radius)
                        val intensity = falloff * point.pressure * INTENSITY_INCREMENT
                        val index = gy * gridWidth + gx
                        newCells[index] = (newCells[index] + intensity).coerceAtMost(maxIntensity)
                    }
                }
            }
        }
        
        return copy(cells = newCells)
    }
    
    /**
     * Returns cells that have zero or near-zero intensity (untouched areas).
     */
    fun getUntouchedCells(threshold: Float = UNTOUCHED_THRESHOLD): List<Pair<Int, Int>> {
        return cells.mapIndexedNotNull { index, intensity ->
            if (intensity < threshold) {
                val x = index % gridWidth
                val y = index / gridWidth
                Pair(x, y)
            } else null
        }
    }
    
    /**
     * Calculates the coverage percentage (how much of the canvas has been touched).
     */
    fun getCoveragePercentage(threshold: Float = UNTOUCHED_THRESHOLD): Float {
        val touchedCount = cells.count { it >= threshold }
        return touchedCount.toFloat() / cells.size
    }
    
    /**
     * Finds the largest contiguous untouched region.
     */
    fun findLargestUntouchedRegion(threshold: Float = UNTOUCHED_THRESHOLD): List<Pair<Int, Int>> {
        val visited = BooleanArray(cells.size)
        var largestRegion = emptyList<Pair<Int, Int>>()
        
        for (y in 0 until gridHeight) {
            for (x in 0 until gridWidth) {
                val index = y * gridWidth + x
                if (!visited[index] && cells[index] < threshold) {
                    val region = floodFill(x, y, threshold, visited)
                    if (region.size > largestRegion.size) {
                        largestRegion = region
                    }
                }
            }
        }
        
        return largestRegion
    }
    
    private fun floodFill(
        startX: Int,
        startY: Int,
        threshold: Float,
        visited: BooleanArray
    ): List<Pair<Int, Int>> {
        val region = mutableListOf<Pair<Int, Int>>()
        val stack = ArrayDeque<Pair<Int, Int>>()
        stack.addLast(Pair(startX, startY))
        
        while (stack.isNotEmpty()) {
            val (x, y) = stack.removeLast()
            val index = y * gridWidth + x
            
            if (x !in 0 until gridWidth || y !in 0 until gridHeight) continue
            if (visited[index]) continue
            if (cells[index] >= threshold) continue
            
            visited[index] = true
            region.add(Pair(x, y))
            
            stack.addLast(Pair(x - 1, y))
            stack.addLast(Pair(x + 1, y))
            stack.addLast(Pair(x, y - 1))
            stack.addLast(Pair(x, y + 1))
        }
        
        return region
    }
    
    companion object {
        const val DEFAULT_GRID_SIZE = 20
        const val DEFAULT_TOUCH_RADIUS = 2
        const val INTENSITY_INCREMENT = 0.3f
        const val UNTOUCHED_THRESHOLD = 0.1f
        
        fun empty(width: Int = DEFAULT_GRID_SIZE, height: Int = DEFAULT_GRID_SIZE): Heatmap {
            return Heatmap(
                gridWidth = width,
                gridHeight = height,
                cells = List(width * height) { 0f }
            )
        }
    }
}
