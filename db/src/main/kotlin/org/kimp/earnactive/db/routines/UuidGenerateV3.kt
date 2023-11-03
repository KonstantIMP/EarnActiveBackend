/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.routines


import java.util.UUID

import org.jooq.Field
import org.jooq.Parameter
import org.jooq.impl.AbstractRoutine
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.kimp.earnactive.db.Public


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UuidGenerateV3 : AbstractRoutine<UUID>("uuid_generate_v3", Public.PUBLIC, SQLDataType.UUID) {
    companion object {

        /**
         * The parameter <code>public.uuid_generate_v3.RETURN_VALUE</code>.
         */
        val RETURN_VALUE: Parameter<UUID?> = Internal.createParameter("RETURN_VALUE", SQLDataType.UUID, false, false)

        /**
         * The parameter <code>public.uuid_generate_v3.namespace</code>.
         */
        val NAMESPACE: Parameter<UUID?> = Internal.createParameter("namespace", SQLDataType.UUID, false, false)

        /**
         * The parameter <code>public.uuid_generate_v3.name</code>.
         */
        val NAME: Parameter<String?> = Internal.createParameter("name", SQLDataType.CLOB, false, false)
    }

    init {
        returnParameter = RETURN_VALUE
        addInParameter(NAMESPACE)
        addInParameter(NAME)
    }

    /**
     * Set the <code>namespace</code> parameter IN value to the routine
     */
    fun setNamespace(value: UUID?): Unit = setValue(NAMESPACE, value)

    /**
     * Set the <code>namespace</code> parameter to the function to be used with
     * a {@link org.jooq.Select} statement
     */
    fun setNamespace(field: Field<UUID?>): Unit {
        setField(NAMESPACE, field)
    }

    /**
     * Set the <code>name</code> parameter IN value to the routine
     */
    fun setName_(value: String?): Unit = setValue(NAME, value)

    /**
     * Set the <code>name</code> parameter to the function to be used with a
     * {@link org.jooq.Select} statement
     */
    fun setName_(field: Field<String?>): Unit {
        setField(NAME, field)
    }
}