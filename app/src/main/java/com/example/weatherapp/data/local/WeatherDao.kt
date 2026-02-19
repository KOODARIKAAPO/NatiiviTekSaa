package com.example.weatherapp.data.local

import androidx.room.*
import com.example.weatherapp.data.model.SearchHistoryEntity
import com.example.weatherapp.data.model.WeatherCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    // UI lukee tästä (Flow)
    @Query("SELECT * FROM weather_cache WHERE queryKey = :key LIMIT 1")
    fun observeCache(key: String): Flow<WeatherCacheEntity?>

    // TTL-tarkistukseen (once)
    @Query("SELECT * FROM weather_cache WHERE queryKey = :key LIMIT 1")
    suspend fun getCacheOnce(key: String): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCache(entity: WeatherCacheEntity)

    @Query("DELETE FROM weather_cache WHERE queryKey = :key")
    suspend fun deleteCache(key: String)

    // Historia
    @Insert
    suspend fun insertHistory(item: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY searchedAtMillis DESC LIMIT 20")
    fun observeHistory(): Flow<List<SearchHistoryEntity>>
}