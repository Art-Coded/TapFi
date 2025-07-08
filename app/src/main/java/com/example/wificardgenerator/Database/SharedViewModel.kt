package com.example.wificardgenerator.Database

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.wificardgenerator.GradientColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {
    private val _savedColors = mutableStateListOf<Color>()
    val solidSavedColors: List<Color> = _savedColors

    // For solid color
    private val _cardColor = MutableStateFlow(Color.White)
    val cardColor: StateFlow<Color?> = _cardColor.asStateFlow()

    //For text color
    private val _textColor = MutableStateFlow(Color.Black)
    val textColor: StateFlow<Color> = _textColor.asStateFlow()

    // For gradient
    private val _cardGradient = MutableStateFlow<GradientColor?>(null)
    val cardGradient: StateFlow<GradientColor?> = _cardGradient.asStateFlow()

    // To track if current selection is gradient
    private val _isGradient = MutableStateFlow(false)
    val isGradient: StateFlow<Boolean> = _isGradient.asStateFlow()

    // For network name details
    private val _networkName = MutableStateFlow("")
    val networkName: StateFlow<String> = _networkName.asStateFlow()

    //For password name details
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun addColor(color: Color) {
        _savedColors.add(0, color)
    }

    fun setCardColor(color: Color) {
        _cardColor.value = color
        _isGradient.value = false
    }

    fun setTextColor(color: Color) {
        _textColor.value = color
    }

    fun setCardGradient(gradient: GradientColor) {
        _cardGradient.value = gradient
        _isGradient.value = true
    }

    fun setNetworkName(name: String) {
        _networkName.value = name
    }

    fun setPassword(pwd: String) {
        _password.value = pwd
    }
}