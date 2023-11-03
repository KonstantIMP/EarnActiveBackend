package org.kimp.earnactive.auth.sms.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
        val id: String,
        val n: Int = 1
)
