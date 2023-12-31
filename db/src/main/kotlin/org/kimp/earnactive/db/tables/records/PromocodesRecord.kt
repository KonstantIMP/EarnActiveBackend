/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.tables.records


import java.time.LocalDate
import java.util.UUID

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record9
import org.jooq.Row9
import org.jooq.impl.UpdatableRecordImpl
import org.kimp.earnactive.db.tables.Promocodes


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class PromocodesRecord() : UpdatableRecordImpl<PromocodesRecord>(Promocodes.PROMOCODES), Record9<UUID?, String?, String?, String?, Int?, Int?, LocalDate?, Int?, String?> {

    open var uuid: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var name: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    open var description: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    open var value: String?
        set(value): Unit = set(3, value)
        get(): String? = get(3) as String?

    open var generalCount: Int?
        set(value): Unit = set(4, value)
        get(): Int? = get(4) as Int?

    open var availableCount: Int?
        set(value): Unit = set(5, value)
        get(): Int? = get(5) as Int?

    open var expirationDate: LocalDate?
        set(value): Unit = set(6, value)
        get(): LocalDate? = get(6) as LocalDate?

    open var cost: Int?
        set(value): Unit = set(7, value)
        get(): Int? = get(7) as Int?

    open var avatarUrl: String?
        set(value): Unit = set(8, value)
        get(): String? = get(8) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    public override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    public override fun fieldsRow(): Row9<UUID?, String?, String?, String?, Int?, Int?, LocalDate?, Int?, String?> = super.fieldsRow() as Row9<UUID?, String?, String?, String?, Int?, Int?, LocalDate?, Int?, String?>
    public override fun valuesRow(): Row9<UUID?, String?, String?, String?, Int?, Int?, LocalDate?, Int?, String?> = super.valuesRow() as Row9<UUID?, String?, String?, String?, Int?, Int?, LocalDate?, Int?, String?>
    public override fun field1(): Field<UUID?> = Promocodes.PROMOCODES.UUID
    public override fun field2(): Field<String?> = Promocodes.PROMOCODES.NAME
    public override fun field3(): Field<String?> = Promocodes.PROMOCODES.DESCRIPTION
    public override fun field4(): Field<String?> = Promocodes.PROMOCODES.VALUE
    public override fun field5(): Field<Int?> = Promocodes.PROMOCODES.GENERAL_COUNT
    public override fun field6(): Field<Int?> = Promocodes.PROMOCODES.AVAILABLE_COUNT
    public override fun field7(): Field<LocalDate?> = Promocodes.PROMOCODES.EXPIRATION_DATE
    public override fun field8(): Field<Int?> = Promocodes.PROMOCODES.COST
    public override fun field9(): Field<String?> = Promocodes.PROMOCODES.AVATAR_URL
    public override fun component1(): UUID? = uuid
    public override fun component2(): String? = name
    public override fun component3(): String? = description
    public override fun component4(): String? = value
    public override fun component5(): Int? = generalCount
    public override fun component6(): Int? = availableCount
    public override fun component7(): LocalDate? = expirationDate
    public override fun component8(): Int? = cost
    public override fun component9(): String? = avatarUrl
    public override fun value1(): UUID? = uuid
    public override fun value2(): String? = name
    public override fun value3(): String? = description
    public override fun value4(): String? = value
    public override fun value5(): Int? = generalCount
    public override fun value6(): Int? = availableCount
    public override fun value7(): LocalDate? = expirationDate
    public override fun value8(): Int? = cost
    public override fun value9(): String? = avatarUrl

    public override fun value1(value: UUID?): PromocodesRecord {
        set(0, value)
        return this
    }

    public override fun value2(value: String?): PromocodesRecord {
        set(1, value)
        return this
    }

    public override fun value3(value: String?): PromocodesRecord {
        set(2, value)
        return this
    }

    public override fun value4(value: String?): PromocodesRecord {
        set(3, value)
        return this
    }

    public override fun value5(value: Int?): PromocodesRecord {
        set(4, value)
        return this
    }

    public override fun value6(value: Int?): PromocodesRecord {
        set(5, value)
        return this
    }

    public override fun value7(value: LocalDate?): PromocodesRecord {
        set(6, value)
        return this
    }

    public override fun value8(value: Int?): PromocodesRecord {
        set(7, value)
        return this
    }

    public override fun value9(value: String?): PromocodesRecord {
        set(8, value)
        return this
    }

    public override fun values(value1: UUID?, value2: String?, value3: String?, value4: String?, value5: Int?, value6: Int?, value7: LocalDate?, value8: Int?, value9: String?): PromocodesRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        this.value8(value8)
        this.value9(value9)
        return this
    }

    /**
     * Create a detached, initialised PromocodesRecord
     */
    constructor(uuid: UUID? = null, name: String? = null, description: String? = null, value: String? = null, generalCount: Int? = null, availableCount: Int? = null, expirationDate: LocalDate? = null, cost: Int? = null, avatarUrl: String? = null): this() {
        this.uuid = uuid
        this.name = name
        this.description = description
        this.value = value
        this.generalCount = generalCount
        this.availableCount = availableCount
        this.expirationDate = expirationDate
        this.cost = cost
        this.avatarUrl = avatarUrl
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised PromocodesRecord
     */
    constructor(value: org.kimp.earnactive.db.tables.pojos.Promocodes?): this() {
        if (value != null) {
            this.uuid = value.uuid
            this.name = value.name
            this.description = value.description
            this.value = value.value
            this.generalCount = value.generalCount
            this.availableCount = value.availableCount
            this.expirationDate = value.expirationDate
            this.cost = value.cost
            this.avatarUrl = value.avatarUrl
            resetChangedOnNotNull()
        }
    }
}
