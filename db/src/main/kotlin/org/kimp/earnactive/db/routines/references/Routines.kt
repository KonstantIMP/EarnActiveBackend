/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.routines.references


import java.util.UUID

import org.jooq.Configuration
import org.jooq.Field
import org.kimp.earnactive.db.routines.UuidGenerateV1
import org.kimp.earnactive.db.routines.UuidGenerateV1mc
import org.kimp.earnactive.db.routines.UuidGenerateV3
import org.kimp.earnactive.db.routines.UuidGenerateV4
import org.kimp.earnactive.db.routines.UuidGenerateV5
import org.kimp.earnactive.db.routines.UuidNil
import org.kimp.earnactive.db.routines.UuidNsDns
import org.kimp.earnactive.db.routines.UuidNsOid
import org.kimp.earnactive.db.routines.UuidNsUrl
import org.kimp.earnactive.db.routines.UuidNsX500



/**
 * Call <code>public.uuid_generate_v1</code>
 */
fun uuidGenerateV1(
      configuration: Configuration
): UUID? {
    val f = UuidGenerateV1()

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_generate_v1</code> as a field.
 */
fun uuidGenerateV1(): Field<UUID?> {
    val f = UuidGenerateV1()

    return f.asField()
}

/**
 * Call <code>public.uuid_generate_v1mc</code>
 */
fun uuidGenerateV1mc(
      configuration: Configuration
): UUID? {
    val f = UuidGenerateV1mc()

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_generate_v1mc</code> as a field.
 */
fun uuidGenerateV1mc(): Field<UUID?> {
    val f = UuidGenerateV1mc()

    return f.asField()
}

/**
 * Call <code>public.uuid_generate_v3</code>
 */
fun uuidGenerateV3(
      configuration: Configuration
    , namespace: UUID?
    , name: String?
): UUID? {
    val f = UuidGenerateV3()
    f.setNamespace(namespace)
    f.setName_(name)

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_generate_v3</code> as a field.
 */
fun uuidGenerateV3(
      namespace: UUID?
    , name: String?
): Field<UUID?> {
    val f = UuidGenerateV3()
    f.setNamespace(namespace)
    f.setName_(name)

    return f.asField()
}

/**
 * Get <code>public.uuid_generate_v3</code> as a field.
 */
fun uuidGenerateV3(
      namespace: Field<UUID?>
    , name: Field<String?>
): Field<UUID?> {
    val f = UuidGenerateV3()
    f.setNamespace(namespace)
    f.setName_(name)

    return f.asField()
}

/**
 * Call <code>public.uuid_generate_v4</code>
 */
fun uuidGenerateV4(
      configuration: Configuration
): UUID? {
    val f = UuidGenerateV4()

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_generate_v4</code> as a field.
 */
fun uuidGenerateV4(): Field<UUID?> {
    val f = UuidGenerateV4()

    return f.asField()
}

/**
 * Call <code>public.uuid_generate_v5</code>
 */
fun uuidGenerateV5(
      configuration: Configuration
    , namespace: UUID?
    , name: String?
): UUID? {
    val f = UuidGenerateV5()
    f.setNamespace(namespace)
    f.setName_(name)

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_generate_v5</code> as a field.
 */
fun uuidGenerateV5(
      namespace: UUID?
    , name: String?
): Field<UUID?> {
    val f = UuidGenerateV5()
    f.setNamespace(namespace)
    f.setName_(name)

    return f.asField()
}

/**
 * Get <code>public.uuid_generate_v5</code> as a field.
 */
fun uuidGenerateV5(
      namespace: Field<UUID?>
    , name: Field<String?>
): Field<UUID?> {
    val f = UuidGenerateV5()
    f.setNamespace(namespace)
    f.setName_(name)

    return f.asField()
}

/**
 * Call <code>public.uuid_nil</code>
 */
fun uuidNil(
      configuration: Configuration
): UUID? {
    val f = UuidNil()

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_nil</code> as a field.
 */
fun uuidNil(): Field<UUID?> {
    val f = UuidNil()

    return f.asField()
}

/**
 * Call <code>public.uuid_ns_dns</code>
 */
fun uuidNsDns(
      configuration: Configuration
): UUID? {
    val f = UuidNsDns()

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_ns_dns</code> as a field.
 */
fun uuidNsDns(): Field<UUID?> {
    val f = UuidNsDns()

    return f.asField()
}

/**
 * Call <code>public.uuid_ns_oid</code>
 */
fun uuidNsOid(
      configuration: Configuration
): UUID? {
    val f = UuidNsOid()

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_ns_oid</code> as a field.
 */
fun uuidNsOid(): Field<UUID?> {
    val f = UuidNsOid()

    return f.asField()
}

/**
 * Call <code>public.uuid_ns_url</code>
 */
fun uuidNsUrl(
      configuration: Configuration
): UUID? {
    val f = UuidNsUrl()

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_ns_url</code> as a field.
 */
fun uuidNsUrl(): Field<UUID?> {
    val f = UuidNsUrl()

    return f.asField()
}

/**
 * Call <code>public.uuid_ns_x500</code>
 */
fun uuidNsX500(
      configuration: Configuration
): UUID? {
    val f = UuidNsX500()

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>public.uuid_ns_x500</code> as a field.
 */
fun uuidNsX500(): Field<UUID?> {
    val f = UuidNsX500()

    return f.asField()
}
