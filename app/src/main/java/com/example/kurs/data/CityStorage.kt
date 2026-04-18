package com.example.kurs.data

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CityStorage(context: Context) {
    private val prefs = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)

    fun saveCities(cities: List<City>) {
        val json = Json.Default.encodeToString(cities)
        prefs.edit().putString("saved_cities", json).apply()
    }

    fun loadCities(): List<City> {
        val json = prefs.getString("saved_cities", null) ?: return emptyList()
        return try { Json.Default.decodeFromString(json) } catch (e: Exception) { emptyList() }
    }
}