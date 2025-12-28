package com.negativespace.puzzler.util

/**
 * Utility functions for the game.
 */
object GameUtils {
    
    /**
     * Formats milliseconds into a readable time string.
     */
    fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / 1000) / 60
        return String.format("%d:%02d", minutes, seconds)
    }
    
    /**
     * Formats a percentage as a string.
     */
    fun formatPercentage(value: Float): String {
        return "${(value * 100).toInt()}%"
    }
    
    /**
     * Calculates the distance between two points.
     */
    fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x2 - x1
        val dy = y2 - y1
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
    
    /**
     * Clamps a value between min and max.
     */
    fun clamp(value: Float, min: Float, max: Float): Float {
        return value.coerceIn(min, max)
    }
    
    /**
     * Linear interpolation between two values.
     */
    fun lerp(start: Float, end: Float, fraction: Float): Float {
        return start + (end - start) * fraction
    }
    
    /**
     * Maps a value from one range to another.
     */
    fun map(
        value: Float,
        fromMin: Float,
        fromMax: Float,
        toMin: Float,
        toMax: Float
    ): Float {
        val normalized = (value - fromMin) / (fromMax - fromMin)
        return toMin + normalized * (toMax - toMin)
    }
}
