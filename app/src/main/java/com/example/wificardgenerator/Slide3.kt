package com.example.wificardgenerator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wificardgenerator.Database.SharedViewModel
import com.example.wificardgenerator.components.WifiCardPreview

@Composable
fun SlideThree(sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WifiCardPreview(sharedViewModel = sharedViewModel)

        Spacer(modifier = Modifier.height(24.dp))

    }
}
