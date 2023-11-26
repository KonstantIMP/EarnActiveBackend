package org.kimp.earnactive.auth.sms.internal

enum class ResponseFormat (
        val number: Int
) {
    AS_STRINGS(0),
    AS_NUMBERS(1),
    XML(2),
    JSON(3)
}
