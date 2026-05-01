package com.example.kurs.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(code: Int?): ImageVector {
    return when (code) {
        0 -> Icons.Rounded.WbSunny
        1, 2 -> Icons.Rounded.WbCloudy
        3 -> Icons.Rounded.Cloud
        45, 48 -> Icons.Rounded.Cloud
        51, 53, 55, 61, 63, 65 -> Icons.Rounded.WaterDrop
        71, 73, 75, 77 -> Icons.Rounded.AcUnit
        80, 81, 82 -> Icons.Rounded.WaterDrop
        95, 96, 99 -> Icons.Rounded.Bolt
        else -> Icons.Rounded.Cloud
    }
}