package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getWeatherByCity(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): WeatherResponse
}
