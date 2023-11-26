package org.kimp.earnactive.auth.controller.model

import java.util.UUID
import kotlin.time.Duration

sealed class TransactionException(
    message: String
): Exception(message)

class TransactionNotFound(
    transactionUuid: UUID
): TransactionException("Unable to find transaction $transactionUuid")

class TooOftenTransactionRequests(
    userUuid: UUID,
    delay: Duration
): TransactionException("Cannot request new transactions for user $userUuid more often that every $delay")

class TransactionExpired(
    transactionUuid: UUID
): TransactionException("Transaction $transactionUuid is inactive due to timeout")
