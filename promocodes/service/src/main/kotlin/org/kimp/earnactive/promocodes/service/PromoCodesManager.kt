package org.kimp.earnactive.promocodes.service

import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DSL.max
import org.jooq.impl.DSL.sum
import org.kimp.earnactive.common.now.NowProvider
import org.kimp.earnactive.db.tables.pojos.Promocodes
import org.kimp.earnactive.db.tables.pojos.UsersPromocodes
import org.kimp.earnactive.db.tables.references.PROMOCODES
import org.kimp.earnactive.db.tables.references.STEPS_CHANGES
import org.kimp.earnactive.db.tables.references.USERS_PROMOCODES
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class PromoCodesManager(
    private val connectionsPool: HikariDataSource,
    private val nowProvider: NowProvider
) {
    fun getAvailablePromoCodes(): List<Promocodes> = connectionContext { ctx ->
        ctx.select().from(PROMOCODES)
            .where(
                PROMOCODES.EXPIRATION_DATE.greaterThan(
                    Instant.ofEpochMilli(nowProvider.nowMillis()).atZone(ZoneId.systemDefault()).toLocalDate()
                )
            )
            .fetch()
            .into(Promocodes::class.java)
    }

    fun getUserBalance(
        userUuid: UUID
    ): Int = connectionContext { ctx ->
        ctx.select(sum(STEPS_CHANGES.DIFF))
            .from(STEPS_CHANGES)
            .where(STEPS_CHANGES.USER_UUID.eq(userUuid))
            .fetchOneInto(Int::class.java)!!
    }

    fun getLastUserTransactionTimestamp(
        userUuid: UUID
    ): LocalDateTime? = connectionContext { ctx ->
        ctx.select(max(STEPS_CHANGES.TIMESTAMP))
            .from(STEPS_CHANGES)
            .where(STEPS_CHANGES.USER_UUID.eq(userUuid))
            .fetchSingle()
            .value1()
    }

    fun addStepsToBalance(
        userUuid: UUID,
        stepsToAdd: Int
    ) {
        connectionContext { ctx ->
            ctx.insertInto(STEPS_CHANGES, STEPS_CHANGES.USER_UUID, STEPS_CHANGES.DIFF)
                .values(userUuid, stepsToAdd)
                .execute()
        }
    }

    fun getUserPromoCodes(
        userUuid: UUID
    ): List<Promocodes> = connectionContext { ctx ->
        ctx.select().from(USERS_PROMOCODES)
            .where(USERS_PROMOCODES.USER_UUID.eq(userUuid))
            .fetchInto(UsersPromocodes::class.java)
            .map { up -> getPromoCodeByUUID(up.promocodeUuid!!) }
    }

    private fun getPromoCodeByUUID(
        promoCodeUUID: UUID
    ): Promocodes = connectionContext { ctx ->
        ctx.select().from(PROMOCODES)
            .where(PROMOCODES.UUID.eq(promoCodeUUID))
            .fetchOneInto(Promocodes::class.java)!!
    }

    fun buyPromoCodeForUser(
        userUuid: UUID,
        promoUuid: UUID
    ): Promocodes? = connectionContext { ctx ->
        val userBalance = getUserBalance(userUuid)
        val promocode = getPromoCodeByUUID(promoUuid)

        if (promocode.cost!! > userBalance || promocode.availableCount!! <= 0) {
            return@connectionContext null
        }

        ctx.update(PROMOCODES)
            .set(PROMOCODES.AVAILABLE_COUNT, PROMOCODES.AVAILABLE_COUNT.minus(1))
            .where(PROMOCODES.UUID.eq(promoUuid))
            .execute()

        ctx.insertInto(USERS_PROMOCODES, USERS_PROMOCODES.USER_UUID, USERS_PROMOCODES.PROMOCODE_UUID)
            .values(userUuid, promoUuid)
            .execute()

        ctx.insertInto(STEPS_CHANGES, STEPS_CHANGES.USER_UUID, STEPS_CHANGES.DIFF)
            .values(userUuid, -promocode.cost!!)
            .execute()

        return@connectionContext promocode
    }

    private fun <T> connectionContext(dslConsumer: (DSLContext) -> T): T {
        return connectionsPool.connection.use { conn ->
            return@use dslConsumer.invoke(
                DSL.using(conn, SQLDialect.POSTGRES)
            )
        }
    }
}
