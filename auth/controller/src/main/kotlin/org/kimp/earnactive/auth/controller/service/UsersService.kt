package org.kimp.earnactive.auth.controller.service

import com.zaxxer.hikari.HikariDataSource
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
    ): Users = context().insertInto(USERS, USERS.PHONE, USERS.NAME)
        .values(PhoneUtils.reduce(phone), name)
        .returning()
        .fetchOne()!!
        .into(Users::class.java)!!

    fun getUserByUUID(uuid: UUID): Users? = context().select()
        .from(USERS)
        .where(USERS.UUID.eq(uuid))
        .fetchOne()
        ?.into(Users::class.java)

    fun getUserByPhone(phone: String): Users? = context().select()
        .from(USERS)
        .where(USERS.PHONE.eq(PhoneUtils.reduce(phone)))
        .fetchOne()
        ?.into(Users::class.java)

    private fun context() = DSL.using(connectionsPool.connection, SQLDialect.POSTGRES)
}
