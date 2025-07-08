package com.example.wificardgenerator

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

object QRCodeGenerator {
    fun generateWifiQRCode(ssid: String, password: String? = null, width: Int = 200, height: Int = 200): Bitmap? {
        val wifiConfig = if (password.isNullOrEmpty()) {
            "WIFI:T:nopass;S:$ssid;;"
        } else {
            "WIFI:T:WPA;S:$ssid;P:$password;;"
        }

        return try {
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                wifiConfig,
                BarcodeFormat.QR_CODE,
                width,
                height
            )
            createBitmap(bitMatrix)
        } catch (e: WriterException) {
            null
        }
    }

    private fun createBitmap(bitMatrix: BitMatrix): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            }
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }


}