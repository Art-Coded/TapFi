package com.example.wificardgenerator.Database

// SharedViewModel.kt
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _savedColors = mutableStateListOf<Color>()
    val savedColors: List<Color> = _savedColors

    fun addColor(color: Color) {
        _savedColors.add(color)
    }
}