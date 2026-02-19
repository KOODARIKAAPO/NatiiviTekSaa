package com.example.weatherapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.remote.RetrofitInstance
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.screens.weather.WeatherScreen
import com.example.weatherapp.ui.screens.weather.WeatherViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.get(applicationContext) }
    private val api by lazy { RetrofitInstance.api } // muokkaa nimeä jos eri
    private val repo by lazy { WeatherRepository(db.weatherDao(), api) }

    private val vm: WeatherViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(repo) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherScreen(vm = vm)
        }
    }
}
