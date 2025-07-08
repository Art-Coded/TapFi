package com.example.wificardgenerator.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class ImagePicker(private val context: Context) {
    fun pickImageFromGallery(onImagePicked: (Uri) -> Unit) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"

        (context as Activity).startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_CODE_GALLERY
        )
    }

    companion object {
        const val REQUEST_CODE_GALLERY = 1001
    }
}

@Composable
fun rememberImagePicker(): ImagePicker {
    val context = LocalContext.current
    return remember { ImagePicker(context) }
}