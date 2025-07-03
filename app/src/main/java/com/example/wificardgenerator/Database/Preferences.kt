package com.example.wificardgenerator.Database

import android.content.Context

class AppPreferences(context: Context) {
    private val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveDarkTheme(enabled: Boolean) {
        sharedPref.edit().putBoolean("dark_theme", enabled).apply()
    }

    fun getDarkTheme(): Boolean {
        return sharedPref.getBoolean("dark_theme", false)
    }
}