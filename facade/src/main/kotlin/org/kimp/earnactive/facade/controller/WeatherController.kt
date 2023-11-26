package org.kimp.earnactive.facade.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import net.devh.boot.grpc.client.inject.GrpcClient
import org.kimp.earnactive.facade.dto.WeatherInfo
import org.kimp.earnactive.weather.api.IEarnAuthWeatherGrpc.IEarnAuthWeatherBlockingStub
import org.kimp.earnactive.weather.api.TWeatherInfoReq
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WeatherController : BaseController() {
    @GrpcClient("weather")
    private lateinit var weatherStub: IEarnAuthWeatherBlockingStub

    @GetMapping("weather")
    @Operation(
        summary = "Возвращает данные о погоде",
        description = "Температуру, тип погоды и картинку, описывающую погодную ситуацию",
        parameters = [
            Parameter(
                name = "OAuth",
                `in` = ParameterIn.HEADER,
                description = "Токен для получения доступа к ресурсам",
                required = true
            ),
            Parameter(
                name = "latitude",
                `in` = ParameterIn.QUERY,
                description = "Широта точки, для которой нужны данные",
                required = true
            ),
            Parameter(
                name = "longitude",
                `in` = ParameterIn.QUERY,
                description = "Долгота точки, для которой нужны данные",
                required = true
            )
        ]
    )
    fun getWeatherInfo(
        @RequestHeader("OAuth", required = true)
        accessToken: String,
        @RequestParam("latitude")
        latitude: Double,
        @RequestParam("longitude")
        longitude: Double
    ): WeatherInfo {
        val weatherInfo = weatherStub.getWeatherInfo(
            TWeatherInfoReq.newBuilder()
                .setAccessToken(accessToken)
                .setLongitude(longitude)
                .setLatitude(latitude)
                .build()
        )
        return WeatherInfo(
            temperature = weatherInfo.temperature,
            backgroundUrl = weatherInfo.backgroundUrl,
            weatherType = weatherInfo.weatherType.name.split("_")[1]
        )
    }
}
