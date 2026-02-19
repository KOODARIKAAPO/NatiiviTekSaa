package com.example.weatherapp.ui.screens.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weatherapp.ui.components.ErrorScreen
import com.example.weatherapp.ui.components.SearchBar
import com.example.weatherapp.ui.components.WeatherContent
import com.example.weatherapp.util.Result
import com.example.weatherapp.data.mapper.toWeatherResponse

@Composable
fun WeatherScreen(vm: WeatherViewModel) {
    val query by vm.searchQuery.collectAsState()
    val state by vm.weatherState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        SearchBar(
            query = query,
            onQueryChange = vm::onSearchQueryChange,
            onSearch = vm::searchWeather
        )

        when (val result = state) {
            is Result.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Result.Success -> {
                val cacheAgeMin = (System.currentTimeMillis() - result.data.fetchedAtMillis) / 60000
                Text("Lähde: Room-cache")
                Text("Cache ikä: ${cacheAgeMin} min")
                Text("Haettu: ${java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Date(result.data.fetchedAtMillis))}")

                WeatherContent(weather = result.data.toWeatherResponse())
            }
            is Result.Error -> {
                ErrorScreen(
                    message = result.message,
                    onRetry = vm::searchWeather
                )
            }
        }
    }
}
