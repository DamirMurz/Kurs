package com.example.kurs.data

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null
)

@Serializable
data class WeatherResponse(
    val current_weather: CurrentWeather? = null,
    val daily: DailyForecast? = null,
    val hourly: HourlyForecast? = null
)

@Serializable
data class HourlyForecast(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val weathercode: List<Int>
)

@Serializable
data class CurrentWeather(
    val temperature: Double,
    val weathercode: Int
)

@Serializable
data class DailyForecast(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val weathercode: List<Int>
)