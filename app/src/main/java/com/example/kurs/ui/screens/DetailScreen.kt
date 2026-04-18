package com.example.kurs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.kurs.R
import com.example.kurs.ui.getWeatherIcon
import com.example.kurs.ui.theme.*
import com.example.kurs.viewmodel.WeatherViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(cityName: String, viewModel: WeatherViewModel, onBack: () -> Unit) {
    val cityData = viewModel.savedCities.find { it.first.name == cityName }
    val weather = cityData?.second

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(cityName, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = WeatherBlue
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = getWeatherIcon(weather?.current_weather?.weathercode),
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconCurrent),
                    tint = Color.White
                )
                Text(
                    text = "${weather?.current_weather?.temperature ?: "--"}°C",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White
                )
            }

            weather?.hourly?.let { hourly ->
                val currentHourStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00"))
                val startIndex = hourly.time.indexOfFirst { it == currentHourStr }.coerceAtLeast(0)

                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.paddingLarge),
                    contentPadding = PaddingValues(horizontal = Dimens.paddingMedium),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
                ) {
                    items(24) { i ->
                        val index = startIndex + i
                        if (index < hourly.time.size) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(hourly.time[index].substring(11, 16), color = Color.White, style = MaterialTheme.typography.labelMedium)
                                Icon(
                                    imageVector = getWeatherIcon(hourly.weathercode[index]),
                                    contentDescription = null,
                                    modifier = Modifier.size(Dimens.iconHourly),
                                    tint = Color.White
                                )
                                Text("${hourly.temperature_2m[index]}°", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = Dimens.cardCornerLarge, topEnd = Dimens.cardCornerLarge),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                LazyColumn(modifier = Modifier.padding(Dimens.paddingMedium)) {
                    item {
                        Text(
                            text = stringResource(R.string.forecast_7_days),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.paddingMedium, top = Dimens.paddingSmall),
                            textAlign = TextAlign.Center
                        )
                    }
                    weather?.daily?.let { daily ->
                        itemsIndexed(daily.time) { index, time ->
                            val formattedDate = time.split("-").let { "${it[2]}.${it[1]}" }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.rowVerticalPadding),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(formattedDate, modifier = Modifier.width(Dimens.dateColumnWidth), style = MaterialTheme.typography.bodyLarge)
                                Icon(
                                    imageVector = getWeatherIcon(daily.weathercode[index]),
                                    contentDescription = null,
                                    modifier = Modifier.size(Dimens.iconDaily).weight(1f),
                                    tint = TextGray
                                )
                                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                                    Text("${daily.temperature_2m_max[index]}°", style = MaterialTheme.typography.bodyLarge)
                                    Text(" / ${daily.temperature_2m_min[index]}°", style = MaterialTheme.typography.bodyMedium, color = TextGray)
                                }
                            }
                            HorizontalDivider(color = DividerGray)
                        }
                    }
                }
            }
        }
    }
}