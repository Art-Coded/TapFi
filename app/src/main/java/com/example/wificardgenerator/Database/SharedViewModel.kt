package com.example.wificardgenerator.Database

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wificardgenerator.BackgroundType
import com.example.wificardgenerator.GradientColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val _savedColors = mutableStateListOf<Color>()
    val solidSavedColors: List<Color> = _savedColors

    // For solid color
    private val _cardColor = MutableStateFlow<Color?>(Color.White)
    val cardColor: StateFlow<Color?> = _cardColor.asStateFlow()

    // For text color
    private val _textColor = MutableStateFlow(Color.Black)
    val textColor: StateFlow<Color> = _textColor.asStateFlow()

    // For gradient
    private val _cardGradient = MutableStateFlow<GradientColor?>(null)
    val cardGradient: StateFlow<GradientColor?> = _cardGradient.asStateFlow()

    // To track if current selection is gradient
    private val _isGradient = MutableStateFlow(false)
    val isGradient: StateFlow<Boolean> = _isGradient.asStateFlow()

    // For background type tracking
    private val _backgroundType = MutableStateFlow(BackgroundType.COLOR)
    val backgroundType: StateFlow<BackgroundType> = _backgroundType.asStateFlow()

    // For network name details
    private val _networkName = MutableStateFlow("")
    val networkName: StateFlow<String> = _networkName.asStateFlow()

    // For password details
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // For background image
    private val _backgroundImage = MutableStateFlow<Bitmap?>(null)
    val backgroundImage: StateFlow<Bitmap?> = _backgroundImage.asStateFlow()

    fun addColor(color: Color) {
        _savedColors.add(0, color)
    }

    fun setCardColor(color: Color) {
        viewModelScope.launch {
            _cardColor.value = color
            _isGradient.value = false
            _backgroundType.value = BackgroundType.COLOR
            // Clear background image when selecting color
            _backgroundImage.value = null
        }
    }

    fun setTextColor(color: Color) {
        viewModelScope.launch {
            _textColor.value = color
        }
    }

    fun setCardGradient(gradient: GradientColor) {
        viewModelScope.launch {
            _cardGradient.value = gradient
            _isGradient.value = true
            _backgroundType.value = BackgroundType.GRADIENT
            // Clear background image when selecting gradient
            _backgroundImage.value = null
        }
    }

    fun setNetworkName(name: String) {
        viewModelScope.launch {
            _networkName.value = name
        }
    }

    fun setPassword(pwd: String) {
        viewModelScope.launch {
            _password.value = pwd
        }
    }

    fun setBackgroundImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _backgroundImage.value = bitmap
            _backgroundType.value = BackgroundType.IMAGE
            // Clear color/gradient when selecting image
            _cardColor.value = null
            _cardGradient.value = null
            _isGradient.value = false
        }
    }

    fun clearBackgroundImage() {
        viewModelScope.launch {
            _backgroundImage.value = null
            // Revert to color background when clearing image
            _backgroundType.value = BackgroundType.COLOR
            _cardColor.value = Color.White
        }
    }
}