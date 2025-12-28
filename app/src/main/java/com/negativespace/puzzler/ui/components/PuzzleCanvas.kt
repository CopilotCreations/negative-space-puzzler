package com.negativespace.puzzler.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.negativespace.puzzler.domain.model.Heatmap
import com.negativespace.puzzler.domain.model.SolutionZone
import com.negativespace.puzzler.domain.model.TouchPoint
import com.negativespace.puzzler.domain.model.ZoneShape
import com.negativespace.puzzler.ui.theme.*

/**
 * The main interactive puzzle canvas where players touch to explore.
 * Renders the heatmap of touches and optionally the solution zone.
 */
@Composable
fun PuzzleCanvas(
    heatmap: Heatmap,
    solutionZone: SolutionZone?,
    showSolution: Boolean,
    onTouch: (TouchPoint) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    highContrastMode: Boolean = false,
    reducedPrecisionMode: Boolean = false
) {
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    
    val heatmapColors = if (highContrastMode) {
        listOf(HighContrastBackground, HighContrastAccent)
    } else {
        listOf(HeatmapCold, HeatmapWarm, HeatmapHot)
    }
    
    val solutionColor = if (highContrastMode) HighContrastAccent else SolutionHighlight
    val solutionBorderColor = if (highContrastMode) HighContrastForeground else SolutionBorder
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (highContrastMode) HighContrastBackground else MaterialTheme.colorScheme.surface)
            .semantics { contentDescription = "Puzzle canvas - tap to interact" }
            .pointerInput(isEnabled) {
                if (isEnabled) {
                    detectTapGestures { offset ->
                        if (canvasSize.width > 0 && canvasSize.height > 0) {
                            val touchPoint = TouchPoint.fromRawCoordinates(
                                rawX = offset.x,
                                rawY = offset.y,
                                canvasWidth = canvasSize.width,
                                canvasHeight = canvasSize.height
                            )
                            onTouch(touchPoint)
                        }
                    }
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            canvasSize = size
            
            // Draw heatmap
            drawHeatmap(
                heatmap = heatmap,
                colors = heatmapColors,
                highContrast = highContrastMode
            )
            
            // Draw solution zone if revealed
            if (showSolution && solutionZone != null) {
                drawSolutionZone(
                    zone = solutionZone,
                    fillColor = solutionColor.copy(alpha = 0.5f),
                    borderColor = solutionBorderColor
                )
            }
            
            // Draw grid lines in reduced precision mode
            if (reducedPrecisionMode) {
                drawAccessibilityGrid(heatmap.gridWidth, heatmap.gridHeight)
            }
        }
    }
}

private fun DrawScope.drawHeatmap(
    heatmap: Heatmap,
    colors: List<Color>,
    highContrast: Boolean
) {
    val cellWidth = size.width / heatmap.gridWidth
    val cellHeight = size.height / heatmap.gridHeight
    
    for (y in 0 until heatmap.gridHeight) {
        for (x in 0 until heatmap.gridWidth) {
            val intensity = heatmap.getIntensity(x, y)
            if (intensity > 0.01f) {
                val color = getHeatmapColor(intensity, colors)
                drawRect(
                    color = color,
                    topLeft = Offset(x * cellWidth, y * cellHeight),
                    size = Size(cellWidth, cellHeight)
                )
            }
        }
    }
}

private fun getHeatmapColor(intensity: Float, colors: List<Color>): Color {
    val clampedIntensity = intensity.coerceIn(0f, 1f)
    
    return when {
        colors.size == 1 -> colors[0].copy(alpha = clampedIntensity)
        colors.size == 2 -> {
            val blend = clampedIntensity
            Color(
                red = colors[0].red + (colors[1].red - colors[0].red) * blend,
                green = colors[0].green + (colors[1].green - colors[0].green) * blend,
                blue = colors[0].blue + (colors[1].blue - colors[0].blue) * blend,
                alpha = 0.3f + clampedIntensity * 0.7f
            )
        }
        else -> {
            val segment = clampedIntensity * (colors.size - 1)
            val index = segment.toInt().coerceIn(0, colors.size - 2)
            val blend = segment - index
            val c1 = colors[index]
            val c2 = colors[index + 1]
            Color(
                red = c1.red + (c2.red - c1.red) * blend,
                green = c1.green + (c2.green - c1.green) * blend,
                blue = c1.blue + (c2.blue - c1.blue) * blend,
                alpha = 0.3f + clampedIntensity * 0.7f
            )
        }
    }
}

private fun DrawScope.drawSolutionZone(
    zone: SolutionZone,
    fillColor: Color,
    borderColor: Color
) {
    val left = zone.left * size.width
    val top = zone.top * size.height
    val width = zone.width * size.width
    val height = zone.height * size.height
    val centerX = zone.centerX * size.width
    val centerY = zone.centerY * size.height
    
    when (zone.shape) {
        ZoneShape.RECTANGLE -> {
            drawRect(
                color = fillColor,
                topLeft = Offset(left, top),
                size = Size(width, height)
            )
            drawRect(
                color = borderColor,
                topLeft = Offset(left, top),
                size = Size(width, height),
                style = Stroke(width = 4f)
            )
        }
        ZoneShape.ELLIPSE -> {
            drawOval(
                color = fillColor,
                topLeft = Offset(left, top),
                size = Size(width, height)
            )
            drawOval(
                color = borderColor,
                topLeft = Offset(left, top),
                size = Size(width, height),
                style = Stroke(width = 4f)
            )
        }
    }
}

private fun DrawScope.drawAccessibilityGrid(gridWidth: Int, gridHeight: Int) {
    val cellWidth = size.width / gridWidth
    val cellHeight = size.height / gridHeight
    val gridColor = Color.Gray.copy(alpha = 0.3f)
    
    // Vertical lines
    for (x in 0..gridWidth) {
        drawLine(
            color = gridColor,
            start = Offset(x * cellWidth, 0f),
            end = Offset(x * cellWidth, size.height),
            strokeWidth = 1f
        )
    }
    
    // Horizontal lines
    for (y in 0..gridHeight) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y * cellHeight),
            end = Offset(size.width, y * cellHeight),
            strokeWidth = 1f
        )
    }
}
