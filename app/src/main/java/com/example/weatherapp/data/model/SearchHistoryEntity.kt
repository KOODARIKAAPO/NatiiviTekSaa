package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history",
    indices = [Index(value = ["queryKey"])]
)
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val queryKey: String,
    val locationName: String,
    val searchedAtMillis: Long = System.currentTimeMillis()
)