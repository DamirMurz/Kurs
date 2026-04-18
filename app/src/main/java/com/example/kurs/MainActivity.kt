package com.example.kurs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kurs.data.CityStorage
import com.example.kurs.ui.screens.DetailScreen
import com.example.kurs.ui.screens.MainScreen
import com.example.kurs.ui.theme.KursTheme
import com.example.kurs.viewmodel.WeatherViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = CityStorage(this)
        val viewModel = WeatherViewModel(storage)

        setContent {
            KursTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(viewModel) { city ->
                            val encodedName = URLEncoder.encode(city.name, StandardCharsets.UTF_8.toString())
                            navController.navigate("detail/$encodedName")
                        }
                    }
                    composable("detail/{cityName}") { backStackEntry ->
                        val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
                        DetailScreen(cityName, viewModel) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}