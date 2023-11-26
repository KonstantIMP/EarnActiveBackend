package org.kimp.earnactive.auth.controller.utils

object PhoneUtils {
    fun reduce(phone: String): String = phone.filter { it.isDigit() }
}
