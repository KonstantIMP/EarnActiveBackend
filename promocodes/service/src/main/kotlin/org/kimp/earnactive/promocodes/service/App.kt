package org.kimp.earnactive.promocodes.service

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.ManagedChannelBuilder
import io.grpc.ServerBuilder
import org.kimp.earnactive.auth.api.IEarnActiveAuthGrpc
import org.kimp.earnactive.common.lock.FileLock
import org.kimp.earnactive.common.now.SystemNowProvider
import java.nio.file.Paths

private val logger = KotlinLogging.logger("PromoCodesService")

fun main() {
    logger.info { "Initialization has started" }

    val config = ConfigFactory.load()!!

    val lock = FileLock(Paths.get(config.getString("service.lockPath")))
    if (!lock.acquire()) {
        logger.error { "Unable to acquire lock at ${lock.lockPath}" }
        return
    }
    logger.info { "Acquired lock at ${lock.lockPath}" }

    logger.info { "Check access by ${config.getString("auth.addr")}:${config.getInt("auth.port")}" }
    val grpcChannel = ManagedChannelBuilder.forAddress(
        config.getString("auth.addr"),
        config.getInt("auth.port")
    ).usePlaintext().build()
    val authStub = IEarnActiveAuthGrpc.newBlockingStub(grpcChannel)

    val hikariDataSource = HikariDataSource(HikariConfig().apply {
        val psqlConfig = config.getConfig("psql")

        dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
        addDataSourceProperty("serverName", psqlConfig.getString("address"))
        addDataSourceProperty("portNumber", psqlConfig.getInt("port"))
        addDataSourceProperty("databaseName", psqlConfig.getString("database"))
        addDataSourceProperty("user", psqlConfig.getString("username"))
        addDataSourceProperty("password", psqlConfig.getString("password"))
    })
    logger.info { "Created PSQL connections pool for ${hikariDataSource.maximumPoolSize} connection(s)" }

    logger.info { "Running weather service at ${config.getInt("service.port")} port" }
    val server = ServerBuilder.forPort(config.getInt("service.port"))
        .addService(
            PromoCodesService(
                authStub,
                PromoCodesManager(hikariDataSource, SystemNowProvider())
            )
        )
        .build()
    server.start()
    server.awaitTermination()

}