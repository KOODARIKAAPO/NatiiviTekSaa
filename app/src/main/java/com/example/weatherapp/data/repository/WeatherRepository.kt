package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.RetrofitInstance
import com.example.weatherapp.util.Result
import retrofit2.HttpException
import java.io.IOException

class WeatherRepository {

    suspend fun getWeather(city: String, apiKey: String): Result<WeatherResponse> {
        return try {
            val response = RetrofitInstance.api.getWeather(
                apiKey = apiKey,
                city = city
            )
            Result.Success(response)
        } catch (e: IOException) {
            Result.Error("Verkkovirhe")
        } catch (e: HttpException) {
            Result.Error("Palvelinvirhe: ${e.code()}")
        } catch (e: Exception) {
            Result.Error("Virhe: ${e.message}")
        }
    }
}
