package com.example.wificardgenerator.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wificardgenerator.Database.SharedViewModel
import com.example.wificardgenerator.QRCodeGenerator

@Composable
fun WifiCardPreview(sharedViewModel: SharedViewModel) {
    val currentCardColor by sharedViewModel.cardColor.collectAsState()
    val currentCardGradient by sharedViewModel.cardGradient.collectAsState()
    val isGradient by sharedViewModel.isGradient.collectAsState()
    val currentTextColor by sharedViewModel.textColor.collectAsState()
    val networkName by sharedViewModel.networkName.collectAsState()
    val password by sharedViewModel.password.collectAsState()
    val backgroundImage by sharedViewModel.backgroundImage.collectAsState()
    val logoImage by sharedViewModel.logoImage.collectAsState()

    val qrCodeBitmap by remember(networkName, password) {
        derivedStateOf {
            QRCodeGenerator.generateWifiQRCode(networkName, password)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        when {
            backgroundImage != null -> {
                Image(
                    bitmap = backgroundImage!!.asImageBitmap(),
                    contentDescription = "Card Background",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }

            isGradient -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    currentCardGradient?.start ?: MaterialTheme.colorScheme.primary,
                                    currentCardGradient?.end ?: MaterialTheme.colorScheme.secondary
                                )
                            ),
                            shape = MaterialTheme.shapes.medium
                        )
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = currentCardColor ?: MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column 1: QR Code
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                if (qrCodeBitmap != null) {
                    Image(
                        bitmap = qrCodeBitmap!!.asImageBitmap(),
                        contentDescription = "WiFi QR Code",
                        modifier = Modifier.size(120.dp)
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            // Column 2: Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (logoImage != null) {
                    Image(
                        bitmap = logoImage!!.asImageBitmap(),
                        contentDescription = "Logo Image",
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                currentTextColor?.let {
                    Text(
                        text = "Network Name (SSID)",
                        style = MaterialTheme.typography.bodySmall,
                        color = it.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                val networkFontSize = when {
                    networkName.length <= 10 -> 22.sp
                    networkName.length <= 14 -> 18.sp
                    networkName.length <= 20 -> 16.sp
                    networkName.length <= 26 -> 14.sp
                    else -> 12.sp
                }

                Text(
                    text = networkName,
                    fontSize = networkFontSize,
                    style = MaterialTheme.typography.headlineMedium,
                    color = currentTextColor,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (password.isNotBlank()) {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodySmall,
                        color = currentTextColor.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    val passwordFontSize = when {
                        password.length <= 12 -> 16.sp
                        password.length <= 20 -> 12.sp
                        else -> 12.sp
                    }

                    Text(
                        text = password,
                        fontSize = passwordFontSize,
                        style = MaterialTheme.typography.titleMedium,
                        color = currentTextColor.copy(alpha = 0.9f),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        lineHeight = 13.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "No password required, free Wifi!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = currentTextColor.copy(alpha = 0.9f),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
