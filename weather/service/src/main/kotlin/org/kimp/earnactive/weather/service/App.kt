package org.kimp.earnactive.weather.service

import com.typesafe.config.ConfigFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.ManagedChannelBuilder
import io.grpc.ServerBuilder
import org.kimp.earnactive.auth.api.IEarnActiveAuthGrpc
import org.kimp.earnactive.common.lock.FileLock
import org.kimp.earnactive.weather.owa.OpenWeatherClient
import java.nio.file.Paths

private val logger = KotlinLogging.logger("WeatherService")

fun main() {
    logger.info { "Initialization has started" }

    val config = ConfigFactory.load()!!

    val lock = FileLock(Paths.get(config.getString("service.lockPath")))
    if (!lock.acquire()) {
        logger.error { "Unable to acquire lock at ${lock.lockPath}" }
        return
    }
    logger.info { "Acquired lock at ${lock.lockPath}" }

    val owaClient = OpenWeatherClient(config.getString("owa.api"))
    logger.info { "Initialized OpenWeather client" }

    val weatherMapper = WeatherMapper(
        config.getConfig("service.weather_backgrounds_urls")
            .entrySet().associate { it.key!! to it.value.unwrapped().toString() },
        config.getString("service.default_weather_background_url")
    )

    logger.info { "Check access by ${config.getString("auth.addr")}:${config.getInt("auth.port")}" }
    val grpcChannel = ManagedChannelBuilder.forAddress(
        config.getString("auth.addr"),
        config.getInt("auth.port")
    ).usePlaintext().build()
    val authStub = IEarnActiveAuthGrpc.newBlockingStub(grpcChannel)

    logger.info { "Running weather service at ${config.getInt("service.port")} port" }
    val server = ServerBuilder.forPort(config.getInt("service.port"))
        .addService(
            WeatherService(
                owaClient,
                authStub,
                weatherMapper
            )
        )
        .build()
    server.start()
    server.awaitTermination()
}
