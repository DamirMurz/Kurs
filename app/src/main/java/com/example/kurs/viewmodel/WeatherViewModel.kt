package com.example.kurs.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurs.data.City
import com.example.kurs.data.CityStorage
import com.example.kurs.network.RetrofitClient
import com.example.kurs.data.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel(private val cityStorage: CityStorage) : ViewModel() {
    var savedCities = mutableStateListOf<Pair<City, WeatherResponse?>>()
    var searchResults = mutableStateOf<List<City>>(emptyList())

    init {
        loadSavedWeather()
    }

    fun search(query: String) {
        if (query.length < 3) return
        viewModelScope.launch {
            try {
                val results = RetrofitClient.ninjaApi.searchCity(query)
                searchResults.value = results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addCity(city: City) {
        if (!savedCities.any { it.first.name == city.name }) {
            viewModelScope.launch {
                try {
                    val weather = RetrofitClient.meteoApi.getWeather(city.latitude, city.longitude)
                    savedCities.add(city to weather)
                    cityStorage.saveCities(savedCities.map { it.first })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadSavedWeather() {
        val cities = cityStorage.loadCities()
        viewModelScope.launch {
            cities.forEach { city ->
                try {
                    val weather = RetrofitClient.meteoApi.getWeather(city.latitude, city.longitude)
                    savedCities.add(city to weather)
                } catch (e: Exception) {
                    savedCities.add(city to null)
                }
            }
        }
    }
    fun removeCity(city: City) {
        savedCities.removeAll { it.first.name == city.name }
        cityStorage.saveCities(savedCities.map { it.first })
    }
}