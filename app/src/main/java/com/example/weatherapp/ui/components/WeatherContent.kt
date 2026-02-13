package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.model.WeatherResponse
import kotlin.math.roundToInt

@Composable
fun WeatherContent(weather: WeatherResponse) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${weather.location.name}, ${weather.location.country}",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "${weather.current.tempC.toInt()} °C",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = weather.current.condition.text
            )
        }
    }
}
