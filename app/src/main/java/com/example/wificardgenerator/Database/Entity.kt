package com.example.wificardgenerator.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_colors")
data class SavedColorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val colorHex: String // e.g. "#FF5733"
)
