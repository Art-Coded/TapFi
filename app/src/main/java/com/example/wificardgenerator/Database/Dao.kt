package com.example.wificardgenerator.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedColorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColor(color: SavedColorEntity)

    @Query("SELECT * FROM saved_colors ORDER BY id DESC")
    fun getAllColors(): Flow<List<SavedColorEntity>>

    @Delete
    suspend fun deleteColor(color: SavedColorEntity)

    @Query("DELETE FROM saved_colors")
    suspend fun deleteAll()
}
