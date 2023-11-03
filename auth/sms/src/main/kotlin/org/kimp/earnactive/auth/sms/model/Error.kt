package org.kimp.earnactive.auth.sms.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val error: String,
    @SerialName("error_code") val errorCode: Int
)
