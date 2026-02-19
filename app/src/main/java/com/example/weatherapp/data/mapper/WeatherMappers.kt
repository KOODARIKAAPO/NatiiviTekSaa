package com.example.weatherapp.data.mapper



import com.example.weatherapp.data.model.Condition
import com.example.weatherapp.data.model.Current
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.model.WeatherCacheEntity
import com.example.weatherapp.data.model.WeatherResponse

fun WeatherCacheEntity.toWeatherResponse(): WeatherResponse =
    WeatherResponse(
        location = Location(
            name = locationName,
            country = country
        ),
        current = Current(
            tempC = tempC,
            condition = Condition(
                text = description,
                icon = iconUrl
            )
        )
    )
