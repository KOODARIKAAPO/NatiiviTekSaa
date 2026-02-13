package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val country: String
)

data class Current(
    @SerializedName("temp_c")
    val tempC: Double,
    val condition: Condition
)

data class Condition(
    val text: String,
    val icon: String
)
