package org.kimp.earnactive.weather.owa

import org.kimp.earnactive.weather.owa.model.Weather

interface OpenWeatherApi {
    suspend fun getWeather(
        longitude: Double,
        latitude: Double
    ): Weather
}
