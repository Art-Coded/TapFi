package com.example.wificardgenerator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wificardgenerator.GradientColor

@Composable
fun HorizontalScrollableColors(
    colors: List<Color>,
    selectedColor: Color?,
    onColorSelected: (Color) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.width(4.dp))

        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .border(
                        width = if (color == selectedColor) 3.dp else 0.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.small
                    )
                    .background(color)
                    .clickable { onColorSelected(color) }
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
fun HorizontalScrollableGradients(
    gradients: List<GradientColor>,
    selectedGradient: GradientColor?,
    onGradientSelected: (GradientColor) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.width(4.dp))

        gradients.forEach { gradient ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .border(
                        width = if (gradient == selectedGradient) 3.dp else 0.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.small
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(gradient.start, gradient.end)
                        )
                    )
                    .clickable { onGradientSelected(gradient) }
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
    }
}

