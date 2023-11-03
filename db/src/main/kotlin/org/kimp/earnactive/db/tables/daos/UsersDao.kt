/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.tables.daos


import java.time.LocalDateTime
import java.util.UUID

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl
import org.kimp.earnactive.db.tables.Users
import org.kimp.earnactive.db.tables.records.UsersRecord


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UsersDao(configuration: Configuration?) : DAOImpl<UsersRecord, org.kimp.earnactive.db.tables.pojos.Users, UUID>(Users.USERS, org.kimp.earnactive.db.tables.pojos.Users::class.java, configuration) {

    /**
     * Create a new UsersDao without any configuration
     */
    constructor(): this(null)

    public override fun getId(o: org.kimp.earnactive.db.tables.pojos.Users): UUID? = o.uuid

    /**
     * Fetch records that have <code>uuid BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfUuid(lowerInclusive: UUID?, upperInclusive: UUID?): List<org.kimp.earnactive.db.tables.pojos.Users> = fetchRange(Users.USERS.UUID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>uuid IN (values)</code>
     */
    fun fetchByUuid(vararg values: UUID): List<org.kimp.earnactive.db.tables.pojos.Users> = fetch(Users.USERS.UUID, *values)

    /**
     * Fetch a unique record that has <code>uuid = value</code>
     */
    fun fetchOneByUuid(value: UUID): org.kimp.earnactive.db.tables.pojos.Users? = fetchOne(Users.USERS.UUID, value)

    /**
     * Fetch records that have <code>phone BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfPhone(lowerInclusive: String?, upperInclusive: String?): List<org.kimp.earnactive.db.tables.pojos.Users> = fetchRange(Users.USERS.PHONE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>phone IN (values)</code>
     */
    fun fetchByPhone(vararg values: String): List<org.kimp.earnactive.db.tables.pojos.Users> = fetch(Users.USERS.PHONE, *values)

    /**
     * Fetch a unique record that has <code>phone = value</code>
     */
    fun fetchOneByPhone(value: String): org.kimp.earnactive.db.tables.pojos.Users? = fetchOne(Users.USERS.PHONE, value)

    /**
     * Fetch records that have <code>name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfName(lowerInclusive: String?, upperInclusive: String?): List<org.kimp.earnactive.db.tables.pojos.Users> = fetchRange(Users.USERS.NAME, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    fun fetchByName(vararg values: String): List<org.kimp.earnactive.db.tables.pojos.Users> = fetch(Users.USERS.NAME, *values)

    /**
     * Fetch records that have <code>creation_timestamp BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    fun fetchRangeOfCreationTimestamp(lowerInclusive: LocalDateTime?, upperInclusive: LocalDateTime?): List<org.kimp.earnactive.db.tables.pojos.Users> = fetchRange(Users.USERS.CREATION_TIMESTAMP, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>creation_timestamp IN (values)</code>
     */
    fun fetchByCreationTimestamp(vararg values: LocalDateTime): List<org.kimp.earnactive.db.tables.pojos.Users> = fetch(Users.USERS.CREATION_TIMESTAMP, *values)
}