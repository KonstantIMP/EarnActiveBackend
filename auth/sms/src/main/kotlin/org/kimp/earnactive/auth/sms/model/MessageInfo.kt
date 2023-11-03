package org.kimp.earnactive.auth.sms.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageInfo(
        val status: Int,
        @SerialName("last_date") val lastDate: String,
        @SerialName("last_timestamp") val lastTimestamp: Long,
        val flag: Int,
        val err: Int = 0,
        @SerialName("send_date") val sendDate: String,
        @SerialName("send_timestamp") val sendTimestamp: Long,
        val phone: String,
        val country: String,
        val operator: String,
        val region: String,
        val cost: Float,
        @SerialName("status_name") val statusName: String,
        val message: String,
        val type: Int
)
