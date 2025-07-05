package com.example.wificardgenerator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorPickerScreen() {

    val colorPickerController = rememberColorPickerController()
    var selectedColor by remember { mutableStateOf(Color.Red) }
    var colorHex by remember {
        mutableStateOf("#${Integer.toHexString(selectedColor.toArgb()).uppercase().takeLast(6)}")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            controller = colorPickerController,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                selectedColor = colorEnvelope.color
                colorHex = "#${Integer.toHexString(selectedColor.toArgb()).uppercase().takeLast(6)}"
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            controller = colorPickerController
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "#${Integer.toHexString(selectedColor.toArgb()).uppercase().takeLast(6)}",
            style = MaterialTheme.typography.bodySmall
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(40.dp)
                .background(selectedColor, shape = MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}