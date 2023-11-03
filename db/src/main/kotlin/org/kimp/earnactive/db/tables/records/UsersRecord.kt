/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.tables.records


import java.time.LocalDateTime
import java.util.UUID

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record4
import org.jooq.Row4
import org.jooq.impl.UpdatableRecordImpl
import org.kimp.earnactive.db.tables.Users


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UsersRecord() : UpdatableRecordImpl<UsersRecord>(Users.USERS), Record4<UUID?, String?, String?, LocalDateTime?> {

    open var uuid: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var phone: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    open var name: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    open var creationTimestamp: LocalDateTime?
        set(value): Unit = set(3, value)
        get(): LocalDateTime? = get(3) as LocalDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    public override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    public override fun fieldsRow(): Row4<UUID?, String?, String?, LocalDateTime?> = super.fieldsRow() as Row4<UUID?, String?, String?, LocalDateTime?>
    public override fun valuesRow(): Row4<UUID?, String?, String?, LocalDateTime?> = super.valuesRow() as Row4<UUID?, String?, String?, LocalDateTime?>
    public override fun field1(): Field<UUID?> = Users.USERS.UUID
    public override fun field2(): Field<String?> = Users.USERS.PHONE
    public override fun field3(): Field<String?> = Users.USERS.NAME
    public override fun field4(): Field<LocalDateTime?> = Users.USERS.CREATION_TIMESTAMP
    public override fun component1(): UUID? = uuid
    public override fun component2(): String? = phone
    public override fun component3(): String? = name
    public override fun component4(): LocalDateTime? = creationTimestamp
    public override fun value1(): UUID? = uuid
    public override fun value2(): String? = phone
    public override fun value3(): String? = name
    public override fun value4(): LocalDateTime? = creationTimestamp

    public override fun value1(value: UUID?): UsersRecord {
        set(0, value)
        return this
    }

    public override fun value2(value: String?): UsersRecord {
        set(1, value)
        return this
    }

    public override fun value3(value: String?): UsersRecord {
        set(2, value)
        return this
    }

    public override fun value4(value: LocalDateTime?): UsersRecord {
        set(3, value)
        return this
    }

    public override fun values(value1: UUID?, value2: String?, value3: String?, value4: LocalDateTime?): UsersRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        return this
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    constructor(uuid: UUID? = null, phone: String? = null, name: String? = null, creationTimestamp: LocalDateTime? = null): this() {
        this.uuid = uuid
        this.phone = phone
        this.name = name
        this.creationTimestamp = creationTimestamp
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    constructor(value: org.kimp.earnactive.db.tables.pojos.Users?): this() {
        if (value != null) {
            this.uuid = value.uuid
            this.phone = value.phone
            this.name = value.name
            this.creationTimestamp = value.creationTimestamp
            resetChangedOnNotNull()
        }
    }
}