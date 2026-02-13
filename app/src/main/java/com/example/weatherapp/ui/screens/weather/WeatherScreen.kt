package com.example.weatherapp.ui.screens.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.components.ErrorScreen
import com.example.weatherapp.ui.components.SearchBar
import com.example.weatherapp.ui.components.WeatherContent
import com.example.weatherapp.util.Result

@Composable
fun WeatherScreen(
    vm: WeatherViewModel = viewModel()
) {
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
                WeatherContent(weather = result.data)
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
