package org.kimp.earnactive.auth.controller.model

import java.util.UUID

data class TransactionInfo(
    val uuid: UUID,
    val code: String,
    val createdTimestamp: Long,
    val expireTimestamp: Long
)
