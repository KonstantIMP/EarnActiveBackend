package org.kimp.earnactive.auth.sms.model

import kotlinx.serialization.Serializable

@Serializable
data class BalanceInfo(
        val balance: Float,
        val currency: String,
        val credit: Float = 0.0f
)
