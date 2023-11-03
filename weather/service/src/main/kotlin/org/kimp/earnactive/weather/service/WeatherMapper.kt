package org.kimp.earnactive.weather.service

import org.kimp.earnactive.weather.api.EWeatherType
import org.kimp.earnactive.weather.api.TWeatherInfoRsp
import org.kimp.earnactive.weather.owa.model.Weather

class WeatherMapper (
    private val weatherTypeToBackgroundUrl: Map<String, String>,
    private val defaultWeatherBackgroundUrl: String
) {
    fun map(w: Weather): TWeatherInfoRsp {
        return TWeatherInfoRsp.newBuilder()
            .setTemperature(w.main.temp)
            .setWeatherType(getWeatherType(w))
            .setBackgroundUrl(getWeatherBackgroundUrl(w))
            .build()
    }

    private fun getWeatherType(w: Weather): EWeatherType {
        if (w.weatherConditions.isEmpty()) return EWeatherType.EWeatherType_UNKNOWN
        return try {
            EWeatherType.valueOf("EWeatherType_${w.weatherConditions[0].main.uppercase()}")
        } catch (e: IllegalArgumentException) {
            EWeatherType.EWeatherType_UNKNOWN
        }
    }

    private fun getWeatherBackgroundUrl(w: Weather): String {
        if (w.weatherConditions.isEmpty()) return defaultWeatherBackgroundUrl
        return weatherTypeToBackgroundUrl[w.weatherConditions[0].main.lowercase()] ?: defaultWeatherBackgroundUrl
    }
}