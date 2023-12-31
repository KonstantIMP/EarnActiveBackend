/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.tables.records


import java.time.LocalDateTime
import java.util.UUID

import org.jooq.Field
import org.jooq.Record3
import org.jooq.Row3
import org.jooq.impl.TableRecordImpl
import org.kimp.earnactive.db.tables.StepsChanges


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class StepsChangesRecord() : TableRecordImpl<StepsChangesRecord>(StepsChanges.STEPS_CHANGES), Record3<UUID?, Int?, LocalDateTime?> {

    open var userUuid: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var diff: Int?
        set(value): Unit = set(1, value)
        get(): Int? = get(1) as Int?

    open var timestamp: LocalDateTime?
        set(value): Unit = set(2, value)
        get(): LocalDateTime? = get(2) as LocalDateTime?

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    public override fun fieldsRow(): Row3<UUID?, Int?, LocalDateTime?> = super.fieldsRow() as Row3<UUID?, Int?, LocalDateTime?>
    public override fun valuesRow(): Row3<UUID?, Int?, LocalDateTime?> = super.valuesRow() as Row3<UUID?, Int?, LocalDateTime?>
    public override fun field1(): Field<UUID?> = StepsChanges.STEPS_CHANGES.USER_UUID
    public override fun field2(): Field<Int?> = StepsChanges.STEPS_CHANGES.DIFF
    public override fun field3(): Field<LocalDateTime?> = StepsChanges.STEPS_CHANGES.TIMESTAMP
    public override fun component1(): UUID? = userUuid
    public override fun component2(): Int? = diff
    public override fun component3(): LocalDateTime? = timestamp
    public override fun value1(): UUID? = userUuid
    public override fun value2(): Int? = diff
    public override fun value3(): LocalDateTime? = timestamp

    public override fun value1(value: UUID?): StepsChangesRecord {
        set(0, value)
        return this
    }

    public override fun value2(value: Int?): StepsChangesRecord {
        set(1, value)
        return this
    }

    public override fun value3(value: LocalDateTime?): StepsChangesRecord {
        set(2, value)
        return this
    }

    public override fun values(value1: UUID?, value2: Int?, value3: LocalDateTime?): StepsChangesRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        return this
    }

    /**
     * Create a detached, initialised StepsChangesRecord
     */
    constructor(userUuid: UUID? = null, diff: Int? = null, timestamp: LocalDateTime? = null): this() {
        this.userUuid = userUuid
        this.diff = diff
        this.timestamp = timestamp
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised StepsChangesRecord
     */
    constructor(value: org.kimp.earnactive.db.tables.pojos.StepsChanges?): this() {
        if (value != null) {
            this.userUuid = value.userUuid
            this.diff = value.diff
            this.timestamp = value.timestamp
            resetChangedOnNotNull()
        }
    }
}
