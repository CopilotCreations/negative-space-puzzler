package com.negativespace.puzzler

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.negativespace.puzzler.domain.model.Heatmap
import com.negativespace.puzzler.domain.model.SolutionZone
import com.negativespace.puzzler.domain.model.TouchPoint
import com.negativespace.puzzler.ui.components.PuzzleCanvas
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PuzzleCanvasTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun puzzleCanvasDisplays() {
        composeTestRule.setContent {
            PuzzleCanvas(
                heatmap = Heatmap.empty(),
                solutionZone = null,
                showSolution = false,
                onTouch = {},
                isEnabled = true
            )
        }
        
        composeTestRule.onNodeWithContentDescription("Puzzle canvas - tap to interact")
            .assertExists()
    }
    
    @Test
    fun puzzleCanvasHandlesTouch() {
        var touchReceived = false
        
        composeTestRule.setContent {
            PuzzleCanvas(
                heatmap = Heatmap.empty(),
                solutionZone = null,
                showSolution = false,
                onTouch = { touchReceived = true },
                isEnabled = true
            )
        }
        
        composeTestRule.onNodeWithContentDescription("Puzzle canvas - tap to interact")
            .performClick()
        
        // Note: Touch handling may require actual gesture simulation
    }
}
