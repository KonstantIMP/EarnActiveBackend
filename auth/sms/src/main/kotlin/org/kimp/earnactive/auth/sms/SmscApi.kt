package org.kimp.earnactive.auth.sms

import org.kimp.earnactive.auth.sms.model.BalanceInfo
import org.kimp.earnactive.auth.sms.model.Message
import org.kimp.earnactive.auth.sms.model.MessageInfo

interface SmscApi {
    suspend fun sendMessage(
        content: String,
        phones: List<String>
    ): Message

    suspend fun getMessageStatus(
        messageId: String,
        phones: List<String>
    ): MessageInfo

    suspend fun getBalance(): BalanceInfo
}
