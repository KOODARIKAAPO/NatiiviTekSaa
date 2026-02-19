package com.example.weatherapp.ui.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherCacheEntity
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.util.Result
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repo: WeatherRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("helsinki")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Room Flow (UI näyttää aina Roomista)
    private val cacheFlow: Flow<WeatherCacheEntity?> =
        searchQuery
            .map { it.trim().lowercase() }
            .distinctUntilChanged()
            .flatMapLatest { key ->
                if (key.isBlank()) flowOf(null) else repo.observeWeather(key)
            }

    private val _weatherState = MutableStateFlow<Result<WeatherCacheEntity>>(Result.Loading)
    val weatherState: StateFlow<Result<WeatherCacheEntity>> = _weatherState.asStateFlow()

    init {
        // Kun Room-cache päivittyy, pusketaan Success UI:lle
        viewModelScope.launch {
            cacheFlow.collect { cache ->
                if (cache != null) _weatherState.value = Result.Success(cache)
            }
        }
    }

    fun onSearchQueryChange(value: String) {
        _searchQuery.value = value
    }

    fun searchWeather() {
        val q = _searchQuery.value.trim()
        if (q.isBlank()) {
            _weatherState.value = Result.Error("Syötä kaupunki")
            return
        }

        viewModelScope.launch {
            _weatherState.value = Result.Loading
            try {
                repo.refreshIfNeeded(q)

                // Jos cache oli jo tuore eikä muuttunut, varmistetaan ettei jää Loading-tilaan
                val cache = repo.getCacheOnce(q)
                if (cache != null) _weatherState.value = Result.Success(cache)
                else _weatherState.value = Result.Error("Ei dataa välimuistissa")
            } catch (t: Throwable) {
                val cache = repo.getCacheOnce(q)
                _weatherState.value =
                    if (cache != null) Result.Success(cache)
                    else Result.Error(t.message ?: "Virhe haettaessa säätä")
            }
        }
    }

    // Historian käyttö. Ei implementoitu
    val history = repo.observeHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
