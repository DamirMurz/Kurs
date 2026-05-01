package com.example.kurs.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private val contentType = "application/json".toMediaType()

    val ninjaApi: ApiNinjasService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ApiNinjasService::class.java)
    }

    val meteoApi: OpenMeteoService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(OpenMeteoService::class.java)
    }
}