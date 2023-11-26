/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.tables.records


import java.util.UUID

import org.jooq.Field
import org.jooq.Record3
import org.jooq.Row3
import org.jooq.impl.TableRecordImpl
import org.kimp.earnactive.db.tables.UsersPromocodes


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UsersPromocodesRecord() : TableRecordImpl<UsersPromocodesRecord>(UsersPromocodes.USERS_PROMOCODES), Record3<UUID?, UUID?, Boolean?> {

    open var userUuid: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var promocodeUuid: UUID?
        set(value): Unit = set(1, value)
        get(): UUID? = get(1) as UUID?

    @Suppress("INAPPLICABLE_JVM_NAME")
    @set:JvmName("setIsUsed")
    open var isUsed: Boolean?
        set(value): Unit = set(2, value)
        get(): Boolean? = get(2) as Boolean?

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    public override fun fieldsRow(): Row3<UUID?, UUID?, Boolean?> = super.fieldsRow() as Row3<UUID?, UUID?, Boolean?>
    public override fun valuesRow(): Row3<UUID?, UUID?, Boolean?> = super.valuesRow() as Row3<UUID?, UUID?, Boolean?>
    public override fun field1(): Field<UUID?> = UsersPromocodes.USERS_PROMOCODES.USER_UUID
    public override fun field2(): Field<UUID?> = UsersPromocodes.USERS_PROMOCODES.PROMOCODE_UUID
    public override fun field3(): Field<Boolean?> = UsersPromocodes.USERS_PROMOCODES.IS_USED
    public override fun component1(): UUID? = userUuid
    public override fun component2(): UUID? = promocodeUuid
    public override fun component3(): Boolean? = isUsed
    public override fun value1(): UUID? = userUuid
    public override fun value2(): UUID? = promocodeUuid
    public override fun value3(): Boolean? = isUsed

    public override fun value1(value: UUID?): UsersPromocodesRecord {
        set(0, value)
        return this
    }

    public override fun value2(value: UUID?): UsersPromocodesRecord {
        set(1, value)
        return this
    }

    public override fun value3(value: Boolean?): UsersPromocodesRecord {
        set(2, value)
        return this
    }

    public override fun values(value1: UUID?, value2: UUID?, value3: Boolean?): UsersPromocodesRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        return this
    }

    /**
     * Create a detached, initialised UsersPromocodesRecord
     */
    constructor(userUuid: UUID? = null, promocodeUuid: UUID? = null, isUsed: Boolean? = null): this() {
        this.userUuid = userUuid
        this.promocodeUuid = promocodeUuid
        this.isUsed = isUsed
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised UsersPromocodesRecord
     */
    constructor(value: org.kimp.earnactive.db.tables.pojos.UsersPromocodes?): this() {
        if (value != null) {
            this.userUuid = value.userUuid
            this.promocodeUuid = value.promocodeUuid
            this.isUsed = value.isUsed
            resetChangedOnNotNull()
        }
    }
}