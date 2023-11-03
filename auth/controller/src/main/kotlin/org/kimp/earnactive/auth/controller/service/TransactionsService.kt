package org.kimp.earnactive.auth.controller.service

import org.kimp.earnactive.auth.controller.model.TooOftenTransactionRequests
import org.kimp.earnactive.auth.controller.model.TransactionExpired
import org.kimp.earnactive.auth.controller.model.TransactionInfo
import org.kimp.earnactive.auth.controller.model.TransactionNotFound
import org.kimp.earnactive.auth.controller.utils.CodesGenerator
import org.kimp.earnactive.common.now.NowProvider
import org.kimp.earnactive.common.now.SystemNowProvider
import redis.clients.jedis.JedisPool
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.DurationUnit

class TransactionsService (
    private val jedisPool: JedisPool,
    private val transactionsDelay: Duration,
    private val transactionExpireDuration: Duration,
    private val codesGenerator: CodesGenerator = CodesGenerator(),
    private val nowProvider: NowProvider = SystemNowProvider()
) {
    fun requestTransactionForUser(userUUID: UUID): TransactionInfo {
        val jedis = jedisPool.resource
        val lastTransaction = (jedis.get("u:$userUUID") ?: "0").toLong()

        if (nowProvider.nowMillis() - lastTransaction < transactionsDelay.toLong(DurationUnit.MILLISECONDS)) {
            throw TooOftenTransactionRequests(userUUID, transactionsDelay)
        }

        val now = nowProvider.nowMillis()
        val transactionInfo = TransactionInfo(
            UUID.randomUUID()!!,
            codesGenerator.generateCode(),
            now,
            now + transactionExpireDuration.toLong(DurationUnit.MILLISECONDS)
        )

        jedis.set("u:$userUUID", transactionInfo.createdTimestamp.toString())
        jedis.hset("t:${transactionInfo.uuid}", mapOf(
            "code" to transactionInfo.code,
            "user" to userUUID.toString(),
            "timestamp" to transactionInfo.createdTimestamp.toString()
        ))

        return transactionInfo
    }

    fun approveTransaction(
        transactionUuid: UUID,
        approveCode: String
    ): UUID? {
        val jedis = jedisPool.resource

        val transactionInfo = jedis.hgetAll("t:$transactionUuid")
            ?: throw TransactionNotFound(transactionUuid)

        if (
            nowProvider.nowMillis() - (transactionInfo["timestamp"] ?: "0").toLong()
                > transactionExpireDuration.toLong(DurationUnit.MILLISECONDS)
        ) {
            throw TransactionExpired(transactionUuid)
        }

        if (approveCode == transactionInfo["code"]) {
            jedis.del("t:$transactionUuid", "code", "user", "timestamp")
            return UUID.fromString(transactionInfo["user"])
        }
        return null
    }
}
