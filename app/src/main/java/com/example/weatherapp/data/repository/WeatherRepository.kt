package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.data.model.SearchHistoryEntity
import com.example.weatherapp.data.model.WeatherCacheEntity
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.BuildConfig
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val weatherDao: WeatherDao,   // <-- NIMI TÄSSÄ
    private val api: WeatherApi
) {
    private val ttlMillis = 30L * 60L * 1000L

    fun observeWeather(query: String): Flow<WeatherCacheEntity?> =
        weatherDao.observeCache(queryKey(query))

    fun observeHistory() = weatherDao.observeHistory()

    suspend fun getCacheOnce(query: String): WeatherCacheEntity? =
        weatherDao.getCacheOnce(queryKey(query))   // <-- käytä weatherDao

    suspend fun refreshIfNeeded(query: String) {
        val key = queryKey(query)
        if (key.isBlank()) return

        val cached = weatherDao.getCacheOnce(key)
        val now = System.currentTimeMillis()
        val stale = cached == null || (now - cached.fetchedAtMillis) > ttlMillis
        if (!stale) return

        val response = api.getWeatherByCity(
            apiKey = BuildConfig.OPENWEATHER_API_KEY,
            city = key
        )

        val entity = WeatherCacheEntity(
            queryKey = key,
            locationName = response.location.name,
            country = response.location.country,
            tempC = response.current.tempC,
            description = response.current.condition.text,
            iconUrl = normalizeIconUrl(response.current.condition.icon),
            fetchedAtMillis = now
        )

        weatherDao.upsertCache(entity)
        weatherDao.insertHistory(
            SearchHistoryEntity(queryKey = key, locationName = "${entity.locationName}, ${entity.country}")
        )
    }

    private fun queryKey(raw: String) = raw.trim().lowercase()

    private fun normalizeIconUrl(icon: String): String =
        when {
            icon.startsWith("http") -> icon
            icon.startsWith("//") -> "https:$icon"
            else -> icon
        }
}
