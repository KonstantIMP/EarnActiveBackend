package org.kimp.earnactive.weather.owa

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kimp.earnactive.weather.owa.model.Weather

class OpenWeatherClient(
    private val apiKey: String,
    val baseUrl: String = "https://api.openweathermap.org"
) : OpenWeatherApi {
    private val jsonObject = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    private val httpClient = HttpClient (CIO) {
        install (ContentNegotiation) {
            json(jsonObject)
        }
    }

    override suspend fun getWeather(longitude: Double, latitude: Double): Weather =
        httpClient.get(baseUrl) {
            url {
                appendPathSegments("data", "2.5", "weather")

                parameters.apply {
                    append("apiKey", apiKey)
                    append("units", "metric")
                    append("lat", latitude.toString())
                    append("lon", longitude.toString())
                }
            }
        }.body()
}
