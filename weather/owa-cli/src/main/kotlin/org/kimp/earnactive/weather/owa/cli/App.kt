package org.kimp.earnactive.weather.owa.cli

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.kimp.earnactive.weather.opa.OpenWeatherClient

val openWeatherApiKey = System.getenv("OPEN_WEATHER_API_KEY") ?: ""

fun main() {
    if (openWeatherApiKey == "") {
        println("Specify OPEN_WEATHER_API_KEY env to continue")
        return
    }

    val client = OpenWeatherClient(openWeatherApiKey)
    runBlocking (Dispatchers.IO) {
        client.getWeather(
            latitude = 59.9311,
            longitude = 30.3609
        ).also { w ->
            println("In Saint-Petersburg it is ${w.main.temp} Celsius degrees")
        }
    }
}
