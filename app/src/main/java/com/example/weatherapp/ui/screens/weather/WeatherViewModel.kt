package com.example.weatherapp.ui.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Aloita "idle"-tyyppisesti error-tekstillä, ettei spinneri pyöri heti
    private val _weatherState =
        MutableStateFlow<Result<WeatherResponse>>(Result.Error("Syötä kaupunki."))
    val weatherState: StateFlow<Result<WeatherResponse>> = _weatherState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchWeather() {
        val city = _searchQuery.value.trim()
        if (city.isBlank()) {
            _weatherState.value = Result.Error("Syötä kaupunki.")
            return
        }

        viewModelScope.launch {
            _weatherState.value = Result.Loading
            _weatherState.value = repository.getWeather(city, BuildConfig.OPENWEATHER_API_KEY)
        }
    }
}
