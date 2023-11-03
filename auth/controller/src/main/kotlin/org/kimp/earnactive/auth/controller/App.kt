package org.kimp.earnactive.auth.controller

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.ServerBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.kimp.earnactive.auth.controller.service.JwtManager
import org.kimp.earnactive.auth.controller.service.TransactionsService
import org.kimp.earnactive.auth.controller.service.UsersService
import org.kimp.earnactive.auth.sms.SmscClient
import org.kimp.earnactive.auth.sms.SmscClientsPool
import org.kimp.earnactive.common.lock.FileLock
import redis.clients.jedis.JedisPool
import java.nio.file.Paths
import kotlin.time.Duration

private val logger = KotlinLogging.logger("AuthController")

fun main() {
    logger.info { "Initialization has started" }

    val config = ConfigFactory.load()!!

    val lock = FileLock(Paths.get(config.getString("service.lockPath")))
    if (!lock.acquire()) {
        logger.error { "Unable to acquire lock at ${lock.lockPath}'" }
        return
    }
    logger.info { "Acquired lock at '${lock.lockPath}'" }

    val redisConfig = config.getConfig("redis")
    val jedisPool = JedisPool(
        redisConfig.getString("address"),
        redisConfig.getInt("port"),
        redisConfig.getString("username"),
        redisConfig.getString("password")
    )
    logger.info { "Created Redis connections pool for ${jedisPool.maxTotal} instance(s)" }

    val smscPool = SmscClientsPool(
        config.getConfigList("smsc").map { smscClientConfig -> SmscClient(
                smscClientConfig.getString("login"),
                smscClientConfig.getString("password")
            )
        }
    )
    logger.info { "Created SMSC clients pool for ${smscPool.clients.size} connection(s)" }

    smscPool.clients.stream().parallel().forEach { c ->
        val balanceInfo = runBlocking (Dispatchers.IO) { c.getBalance() }
        logger.info { "[SMSC] User '${c.login} has ${balanceInfo.balance} ${balanceInfo.currency}" }
    }

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

    val authTokenExpireDuration = Duration.parse(config.getString("service.jwt.auth_token_expire_duration"))
    val refreshTokenExpireDuration = Duration.parse(config.getString("service.jwt.refresh_token_expire_duration"))

    logger.info {
        "[JWT] Auth tokens will be valid for $authTokenExpireDuration and refresh for $refreshTokenExpireDuration"
    }
    val jwtManager = JwtManager(
        config.getString("service.jwt.secret"),
        authTokenExpireDuration,
        refreshTokenExpireDuration
    )

    val usersService = UsersService(hikariDataSource)

    val transactionsService = TransactionsService(
        jedisPool,
        Duration.parse(config.getString("service.transaction.request_delay")),
        Duration.parse(config.getString("service.transaction.expire_duration"))
    )

    val port = config.getInt("service.port")
    logger.info { "Running auth controller at $port port" }

    val server = ServerBuilder.forPort(port)
        .addService(AuthController(
            smscPool,
            jwtManager,
            usersService,
            transactionsService
        ))
        .build()
    server.start()
    server.awaitTermination()
}
