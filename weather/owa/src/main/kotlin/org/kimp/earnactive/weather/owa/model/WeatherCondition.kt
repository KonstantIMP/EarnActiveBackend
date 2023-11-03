package org.kimp.earnactive.weather.owa.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
