package com.example.wificardgenerator

import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wificardgenerator.Database.SharedViewModel
import com.example.wificardgenerator.components.HorizontalScrollableColors
import com.example.wificardgenerator.components.HorizontalScrollableGradients

data class GradientColor(
    val start: Color,
    val end: Color,
    val name: String
)

@Composable
fun SlideTwo(
    colorPickerClick: () -> Unit,
    sharedViewModel: SharedViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {

    val currentCardColor by sharedViewModel.cardColor.collectAsState()
    val currentCardGradient by sharedViewModel.cardGradient.collectAsState()
    val isGradient by sharedViewModel.isGradient.collectAsState()
    val currentTextColor by sharedViewModel.textColor.collectAsState()

    val networkName by sharedViewModel.networkName.collectAsState()
    val password by sharedViewModel.password.collectAsState()

    val qrCodeBitmap by remember(networkName, password) {
        derivedStateOf {
            QRCodeGenerator.generateWifiQRCode(networkName, password)
        }
    }

    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val backgroundImage by sharedViewModel.backgroundImage.collectAsState()
    val logoImage by sharedViewModel.logoImage.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                sharedViewModel.setBackgroundImage(bitmap)
            }
        }
    )

    val logoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                sharedViewModel.setLogoImage(bitmap)
            }
        }
    )

    val backgroundType by sharedViewModel.backgroundType.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_customize),
                contentDescription = "WiFi Icon",
                modifier = Modifier
                    .size(42.dp)
                    .padding(end = 8.dp, bottom = 8.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )

            Text(
                text = "Customize your Card",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp),
        ) {

            Text(
                text = "Preview",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                if (backgroundImage != null) {
                    Image(
                        bitmap = backgroundImage!!.asImageBitmap(),
                        contentDescription = "Card Background",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                } else if (isGradient) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        currentCardGradient?.start ?: Color.Blue,
                                        currentCardGradient?.end ?: Color.Cyan
                                    )
                                ),
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = currentCardColor ?: MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Column 1: QR Code (Left Side)
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

                    // Column 2: Network Info (Right Side)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        if (logoImage != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Image(
                                    bitmap = logoImage!!.asImageBitmap(),
                                    contentDescription = "Logo Image",
                                    modifier = Modifier.size(50.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }


                        // Network Name Label
                        currentTextColor?.let {
                            Text(
                                text = "Network Name (SSID)",
                                style = MaterialTheme.typography.bodySmall,
                                color = it.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        // Dynamic Network Name Text Size
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
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (password.isNotBlank()) {
                            Text(
                                text = "Password",
                                style = MaterialTheme.typography.bodySmall,
                                color = currentTextColor.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
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
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                lineHeight = 13.sp
                            )
                        } else {
                            Text(
                                text = "No password required, free Wifi!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = currentTextColor.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp),
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                            )

                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pick your card color",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Solid colors",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Row {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable { colorPickerClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.paint),
                        contentDescription = "Paint Icon",
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                val presetSolidColors = remember {
                    listOf(
                        Color(0xFFFFFFFF), // White
                        Color(0xFF000000), // Black
                        Color(0xFFF44336), // Red
                        Color(0xFFE91E63), // Pink
                        Color(0xFF9C27B0), // Purple
                        Color(0xFF673AB7), // Deep Purple
                        Color(0xFF3F51B5), // Indigo
                        Color(0xFF2196F3), // Blue
                        Color(0xFF00BCD4), // Cyan
                        Color(0xFF4CAF50)  // Green
                    )
                }

                HorizontalScrollableColors(
                    colors = sharedViewModel.solidSavedColors + presetSolidColors,
                    selectedColor = if (!isGradient && backgroundType == BackgroundType.COLOR) currentCardColor else null,
                    onColorSelected = { color ->
                        sharedViewModel.setCardColor(color)
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Gradient colors",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Row {
                val gradientColors = remember {
                    listOf(
                        GradientColor(
                            start = Color(0xFFF6D242),
                            end = Color(0xFFFF52E5),
                            name = "Sunset"
                        ),
                        GradientColor(
                            start = Color(0xFF2193B0),
                            end = Color(0xFF6DD5ED),
                            name = "Ocean"
                        ),
                        GradientColor(
                            start = Color(0xFFF857A6),
                            end = Color(0xFFFF5858),
                            name = "Pinky"
                        ),
                        GradientColor(
                            start = Color(0xFFA8FF78),
                            end = Color(0xFF78FFD6),
                            name = "Mint"
                        ),
                        GradientColor(
                            start = Color(0xFFDA22FF),
                            end = Color(0xFF9733EE),
                            name = "Purple"
                        ),
                        GradientColor(
                            start = Color(0xFFFF512F),
                            end = Color(0xFFDD2476),
                            name = "Bloody"
                        ),
                        GradientColor(
                            start = Color(0xFF1A2980),
                            end = Color(0xFF26D0CE),
                            name = "Aqua"
                        ),
                        GradientColor(
                            start = Color(0xFFFF8008),
                            end = Color(0xFFFFC837),
                            name = "Sunshine"
                        ),
                        GradientColor(
                            start = Color(0xFF4776E6),
                            end = Color(0xFF8E54E9),
                            name = "Royal"
                        ),
                        GradientColor(
                            start = Color(0xFF114357),
                            end = Color(0xFFF29492),
                            name = "Wine"
                        ),
                        GradientColor(
                            start = Color(0xFF43CEA2),
                            end = Color(0xFF185A9D),
                            name = "Turquoise"
                        ),
                        GradientColor(
                            start = Color(0xFF00C9FF),
                            end = Color(0xFF92FE9D),
                            name = "Sky Mint"
                        ),
                        GradientColor(
                            start = Color(0xFF3CA55C),
                            end = Color(0xFFB5AC49),
                            name = "Lime Forest"
                        ),
                        GradientColor(
                            start = Color(0xFFFF5F6D),
                            end = Color(0xFFFFC371),
                            name = "Coral"
                        ),
                        GradientColor(
                            start = Color(0xFF654EA3),
                            end = Color(0xFFEAAFC8),
                            name = "Lavender"
                        ),
                        GradientColor(
                            start = Color(0xFF009FFF),
                            end = Color(0xFFEC2F4B),
                            name = "Fire Ice"
                        ),
                        GradientColor(
                            start = Color(0xFFFC5C7D),
                            end = Color(0xFF6A82FB),
                            name = "Peachberry"
                        ),
                        GradientColor(
                            start = Color(0xFF12C2E9),
                            end = Color(0xFFC471ED),
                            name = "Blueberry"
                        ),
                        GradientColor(
                            start = Color(0xFFFF6CAB),
                            end = Color(0xFF7366FF),
                            name = "Candy"
                        ),
                        GradientColor(
                            start = Color(0xFF00F260),
                            end = Color(0xFF0575E6),
                            name = "Fresh Leaf"
                        )
                    )
                }

                HorizontalScrollableGradients(
                    gradients = gradientColors,
                    selectedGradient = if (backgroundType == BackgroundType.GRADIENT) currentCardGradient else null,
                    onGradientSelected = { gradient ->
                        sharedViewModel.setCardGradient(gradient)
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(48.dp)
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    )
                    .clickable {
                        galleryLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add a Photo Background",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pick your Text Color",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Default",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 12.dp)
            )

            Row {

                val presetSolidColors = remember {
                    listOf(
                        Color(0xFFFFFFFF), // White
                        Color(0xFF000000), // Black
                        Color(0xFFF44336), // Red
                        Color(0xFFE91E63), // Pink
                        Color(0xFF9C27B0), // Purple
                        Color(0xFF673AB7), // Deep Purple
                        Color(0xFF3F51B5), // Indigo
                        Color(0xFF2196F3), // Blue
                        Color(0xFF00BCD4), // Cyan
                        Color(0xFF4CAF50)  // Green
                    )
                }

                HorizontalScrollableColors(
                    colors = presetSolidColors,
                    selectedColor = currentTextColor,
                    onColorSelected = { color ->
                        sharedViewModel.setTextColor(color)
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Add a Logo",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 4.dp)
                )

                Text(
                    text = "(Optional)",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (logoImage == null) {
                // Show Open Gallery button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(48.dp)
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        )
                        .clickable {
                            logoLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Open gallery",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                // Show Remove Logo button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(48.dp)
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        )
                        .clickable {
                            sharedViewModel.clearLogoImage()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Remove logo",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Delete icon",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    onBackClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = "Back")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    onNextClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = "Next")
            }
        }


        Spacer(modifier = Modifier.height(50.dp))
    }
}