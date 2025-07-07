package com.example.wificardgenerator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wificardgenerator.Database.SharedViewModel


@Composable
fun SlideOne(onNextClick: () -> Unit, sharedViewModel: SharedViewModel) {

    val scrollState = rememberScrollState()

    // Get values from ViewModel
    val networkNameState by sharedViewModel.networkName.collectAsState()
    val passwordState by sharedViewModel.password.collectAsState()

    // Local state for error handling and UI updates
    var networkName by remember { mutableStateOf(networkNameState) }
    var passwordName by remember { mutableStateOf(passwordState) }
    var currentError by remember { mutableStateOf("") }

    var networkNameError by remember { mutableStateOf(false) }
    var passwordNameError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val lastNameFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val networkMaxLength = 20
    val passwordMaxLength = 30

    fun validateFields(): Boolean {
        return when {
            networkName.isBlank() -> {
                currentError = "Network name field cannot be empty."
                networkNameError = true
                passwordNameError = false
                false
            }
            else -> {
                currentError = ""
                networkNameError = false
                passwordNameError = false
                true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_wifi3),
                contentDescription = "WiFi Icon",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp, bottom = 8.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )

            Text(
                text = "WiFi Card Generator",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Create and share your WiFi credentials easily",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(16.dp),
        ) {
            Text(
                text = "Enter Your WiFi Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "This information will be encoded in the QR code",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Network Name (SSID)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = networkName,
                    onValueChange = {
                        if (it.length <= networkMaxLength) {
                            networkName = it
                            networkNameError = false
                        }
                    },
                    label = { Text("Network Name (SSID)") },
                    singleLine = true,
                    isError = networkNameError,
                    supportingText = {
                        Text(
                            text = if (networkNameError) currentError else " ",
                            color = if (networkNameError) MaterialTheme.colorScheme.error else Color.Transparent
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 0.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            lastNameFocusRequester.requestFocus()
                        }
                    )
                )

                Text(
                    text = "${networkName.length} / $networkMaxLength",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            Text(
                text = "Password",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 4.dp)
            )

            Text(
                text = "Leave empty if your network doesn't have a password",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = passwordName,
                    onValueChange = {
                        if (it.length <= passwordMaxLength) {
                            passwordName = it
                            passwordNameError = false
                        }
                    },
                    label = { Text("Password") },
                    singleLine = true,
                    isError = passwordNameError,
                    supportingText = {
                        Text(
                            text = if (passwordNameError) currentError else " ",
                            color = if (passwordNameError) MaterialTheme.colorScheme.error else Color.Transparent
                        )
                    }
                    ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(lastNameFocusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                Text(
                    text = "${passwordName.length} / $passwordMaxLength",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "Security Tip",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "This QR code generator works entirely in your browser. Your WiFi credentials are never sent to any server.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (validateFields()) {
                    sharedViewModel.setNetworkName(networkName)
                    sharedViewModel.setPassword(passwordName)
                    onNextClick()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 12.dp)
        ) {
            Text(text = "Next")
        }


        Spacer(modifier = Modifier.height(50.dp))
    }
}

