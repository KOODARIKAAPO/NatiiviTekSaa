package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val queryKey: String,
    val locationName: String,
    val country: String,
    val tempC: Double,
    val description: String,
    val iconUrl: String,
    val fetchedAtMillis: Long
)
