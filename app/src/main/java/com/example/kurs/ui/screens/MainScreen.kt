package com.example.kurs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.example.kurs.R
import com.example.kurs.data.City
import com.example.kurs.data.WeatherResponse
import com.example.kurs.ui.getWeatherIcon
import com.example.kurs.ui.theme.*
import com.example.kurs.viewmodel.WeatherViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: WeatherViewModel, onCityClick: (City) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WeatherBlue, WeatherLightBackground)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(Dimens.paddingMedium)
            ) {
                Text(
                    text = stringResource(R.string.today_weather, formattedDate),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.length > 2) viewModel.search(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.search_placeholder), color = SearchBarPlaceholder) },
                    leadingIcon = { Icon(Icons.Rounded.Search, null, tint = SearchBarIcon) },
                    shape = RoundedCornerShape(Dimens.searchBarCorner),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SearchBarContainerFocused,
                        unfocusedContainerColor = SearchBarContainer,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White
                    ),
                    singleLine = true
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    if (viewModel.searchResults.value.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Dimens.paddingSmall)
                                .zIndex(1f),
                            shape = RoundedCornerShape(Dimens.cardCornerSmall),
                            elevation = CardDefaults.cardElevation(Dimens.elevationLarge)
                        ) {
                            LazyColumn(modifier = Modifier.heightIn(max = Dimens.searchResultMaxHeight)) {
                                items(viewModel.searchResults.value) { city ->
                                    ListItem(
                                        headlineContent = { Text(city.name) },
                                        supportingContent = { Text(city.country ?: "") },
                                        modifier = Modifier.clickable {
                                            viewModel.addCity(city)
                                            searchQuery = ""
                                            viewModel.searchResults.value = emptyList()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(Dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
            ) {
                items(viewModel.savedCities) { (city, weather) ->
                    CityWeatherCard(
                        city = city,
                        weather = weather,
                        onClick = { onCityClick(city) },
                        onDelete = { viewModel.removeCity(city) }
                    )
                }
            }
        }
    }
}

@Composable
fun CityWeatherCard(
    city: City,
    weather: WeatherResponse?,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(Dimens.cardCornerMedium),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(Dimens.elevationSmall)
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingMedium)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(city.name, style = MaterialTheme.typography.titleLarge, maxLines = 1)
                    Text(city.country ?: "", style = MaterialTheme.typography.labelSmall, color = TextGray)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${weather?.current_weather?.temperature ?: "--"}°",
                        style = MaterialTheme.typography.displaySmall,
                        color = WeatherBlue
                    )

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = Color.Red.copy(alpha = 0.6f),
                            modifier = Modifier.size(Dimens.iconSmall)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            weather?.hourly?.let { hourly ->
                val currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00"))
                val startIndex = hourly.time.indexOfFirst { it == currentHour }.coerceAtLeast(0)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(8) { i ->
                        val idx = startIndex + i
                        if (idx < hourly.time.size) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(hourly.time[idx].substring(11, 16), style = MaterialTheme.typography.labelSmall, color = TextGray)
                                Icon(
                                    imageVector = getWeatherIcon(hourly.weathercode[idx]),
                                    contentDescription = null,
                                    modifier = Modifier.size(Dimens.iconSmall),
                                    tint = WeatherBlue
                                )
                                Text("${hourly.temperature_2m[idx]}°", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}