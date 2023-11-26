package org.kimp.earnactive.facade.dto

data class TransactionResponse(
    val transactionUuid: String,
    val isNew: Boolean = false
)
