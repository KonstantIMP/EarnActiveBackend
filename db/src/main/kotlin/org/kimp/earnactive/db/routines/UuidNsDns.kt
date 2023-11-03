/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.routines


import java.util.UUID

import org.jooq.Parameter
import org.jooq.impl.AbstractRoutine
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.kimp.earnactive.db.Public


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UuidNsDns : AbstractRoutine<UUID>("uuid_ns_dns", Public.PUBLIC, SQLDataType.UUID) {
    companion object {

        /**
         * The parameter <code>public.uuid_ns_dns.RETURN_VALUE</code>.
         */
        val RETURN_VALUE: Parameter<UUID?> = Internal.createParameter("RETURN_VALUE", SQLDataType.UUID, false, false)
    }

    init {
        returnParameter = RETURN_VALUE
    }
}
