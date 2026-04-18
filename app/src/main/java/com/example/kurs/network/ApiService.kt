package com.example.kurs.network

import com.example.kurs.BuildConfig
import com.example.kurs.data.City
import com.example.kurs.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiNinjasService {
    @GET("v1/city")
    suspend fun searchCity(
        @Query("name") name: String,
        @Header("X-Api-Key") apiKey: String = BuildConfig.NINJA_KEY
    ): List<City>
}

interface OpenMeteoService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current_weather") current: Boolean = true,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weathercode",
        @Query("hourly") hourly: String = "temperature_2m,weathercode",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}