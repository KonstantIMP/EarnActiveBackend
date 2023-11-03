package org.kimp.earnactive.weather.owa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    @SerialName("coord")
    val coordinates: Coordinates,
    @SerialName("weather")
    val weatherConditions: List<WeatherCondition>,
    val main: WeatherData,
    val visibility: Double,
    val wind: WindData
)
