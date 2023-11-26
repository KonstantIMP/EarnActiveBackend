package org.kimp.earnactive.auth.controller.service

import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.kimp.earnactive.auth.controller.utils.PhoneUtils
import org.kimp.earnactive.db.tables.pojos.Users
import org.kimp.earnactive.db.tables.references.USERS
import java.util.UUID

class UsersService (
    private val connectionsPool: HikariDataSource
) {
    fun createUser(
        phone: String,
        name: String
    ): Users = connectionContext { ctx ->
        ctx.insertInto(USERS, USERS.PHONE, USERS.NAME)
            .values(PhoneUtils.reduce(phone), name)
            .returning()
            .fetchOne()!!
            .into(Users::class.java)!!
    }

    fun getUserByUUID(uuid: UUID): Users? = connectionContext { ctx ->
        ctx.select()
            .from(USERS)
            .where(USERS.UUID.eq(uuid))
            .fetchOne()
            ?.into(Users::class.java)
    }

    fun getUserByPhone(phone: String): Users? = connectionContext { ctx ->
        ctx.select()
            .from(USERS)
            .where(USERS.PHONE.eq(PhoneUtils.reduce(phone)))
            .fetchOne()
            ?.into(Users::class.java)
    }

    fun setUserName(userUUID: UUID, name: String) {
        connectionContext { ctx ->
            ctx.update(USERS)
                .set(USERS.NAME, name)
                .where(USERS.UUID.eq(userUUID))
                .execute()
        }
    }

    private fun <T> connectionContext(dslConsumer: (DSLContext) -> T): T {
        return connectionsPool.connection.use { conn ->
            return@use dslConsumer.invoke(
                DSL.using(conn, SQLDialect.POSTGRES)
            )
        }
    }
}
