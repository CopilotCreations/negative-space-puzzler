package com.negativespace.puzzler.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a single touch point on the canvas.
 * All coordinates are normalized to 0.0-1.0 range for device independence.
 */
@Serializable
data class TouchPoint(
    val x: Float,
    val y: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val pressure: Float = 1.0f
) {
    init {
        require(x in 0f..1f) { "X coordinate must be normalized (0-1)" }
        require(y in 0f..1f) { "Y coordinate must be normalized (0-1)" }
        require(pressure in 0f..1f) { "Pressure must be normalized (0-1)" }
    }
    
    companion object {
        fun fromRawCoordinates(
            rawX: Float,
            rawY: Float,
            canvasWidth: Float,
            canvasHeight: Float,
            pressure: Float = 1.0f
        ): TouchPoint {
            return TouchPoint(
                x = (rawX / canvasWidth).coerceIn(0f, 1f),
                y = (rawY / canvasHeight).coerceIn(0f, 1f),
                pressure = pressure.coerceIn(0f, 1f)
            )
        }
    }
}
