package org.kimp.earnactive.auth.controller

import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.jsonwebtoken.JwtException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.kimp.earnactive.auth.api.EJwtTokenType
import org.kimp.earnactive.auth.api.IEarnActiveAuthGrpc.IEarnActiveAuthImplBase
import org.kimp.earnactive.auth.api.TAuthUserReq
import org.kimp.earnactive.auth.api.TAuthUserRsp
import org.kimp.earnactive.auth.api.TConfirmUserReq
import org.kimp.earnactive.auth.api.TConfirmUserRsp
import org.kimp.earnactive.auth.api.TCreateUserReq
import org.kimp.earnactive.auth.api.TCreateUserRsp
import org.kimp.earnactive.auth.api.TCredentials
import org.kimp.earnactive.auth.api.TGetUserInfoReq
import org.kimp.earnactive.auth.api.TGetUserInfoRsp
import org.kimp.earnactive.auth.api.TRefreshTokenReq
import org.kimp.earnactive.auth.api.TRefreshTokenRsp
import org.kimp.earnactive.auth.api.TSetNameReq
import org.kimp.earnactive.auth.api.TSetNameRsp
import org.kimp.earnactive.auth.api.TTransaction
import org.kimp.earnactive.auth.controller.model.TooOftenTransactionRequests
import org.kimp.earnactive.auth.controller.model.TransactionExpired
import org.kimp.earnactive.auth.controller.model.TransactionNotFound
import org.kimp.earnactive.auth.controller.service.JwtManager
import org.kimp.earnactive.auth.controller.service.TransactionsService
import org.kimp.earnactive.auth.controller.service.UsersService
import org.kimp.earnactive.auth.sms.SmscClientsPool
import org.kimp.earnactive.common.now.NowProvider
import org.kimp.earnactive.common.now.SystemNowProvider
import org.kimp.earnactive.utils.millisToTimestamp
import org.kimp.earnactive.utils.toMillis
import java.time.ZoneOffset
import java.util.UUID
import kotlin.time.DurationUnit

class AuthController (
    private val smscClientsPool: SmscClientsPool,
    private val jwtManager: JwtManager,
    private val usersService: UsersService,
    private val transactionsService: TransactionsService,
    private val nowProvider: NowProvider = SystemNowProvider()
) : IEarnActiveAuthImplBase() {

    override fun authUser(
        request: TAuthUserReq,
        responseObserver: StreamObserver<TAuthUserRsp>
    ) {
        try {
            var isNew = false

            val user = usersService.getUserByPhone(request.phone)
                ?: usersService.createUser(request.phone, "").also { isNew = true }

            if (user.name?.isEmpty() != false) isNew = true

            val transaction = transactionsService.requestTransactionForUser(user.uuid!!)
            responseObserver.onNext(
                TAuthUserRsp.newBuilder()
                    .setUserAuthTransaction(
                        TTransaction.newBuilder()
                            .setUuid(transaction.uuid.toString())
                            .setDeadTime(transaction.expireTimestamp.millisToTimestamp())
                            .build()
                    )
                    .setIsNew(isNew)
                    .build()
            )
            responseObserver.onCompleted()

            sendApproveCode(user.phone!!, transaction.code)
        } catch (e: TooOftenTransactionRequests) {
            responseObserver.onError(Status.ABORTED.withCause(e).asRuntimeException())
        }
    }

    override fun setName(
        request: TSetNameReq,
        responseObserver: StreamObserver<TSetNameRsp>
    ) {
        val jwtContent = jwtManager.extractJwtContent(request.accessToken)

        if (jwtContent.tokenType != EJwtTokenType.EJwtTokenType_AUTH) {
            responseObserver.onError(
                Status.UNAUTHENTICATED
                    .withDescription("Use auth token to get user credentials")
                    .asRuntimeException()
            )
            return
        }

        if (jwtContent.expirationTimestamp.toMillis() <= nowProvider.nowMillis()) {
            responseObserver.onError(
                Status.UNAUTHENTICATED
                    .withDescription("Token expired")
                    .asRuntimeException()
            )
            return
        }

        val user = usersService.getUserByUUID(UUID.fromString(jwtContent.userUuid))
        usersService.setUserName(user!!.uuid!!, request.name)

        responseObserver.onNext(TSetNameRsp.getDefaultInstance())
        responseObserver.onCompleted()
    }

    override fun createUser(
        request: TCreateUserReq,
        responseObserver: StreamObserver<TCreateUserRsp>
    ) {
        if (usersService.getUserByPhone(request.phone) != null) {
            responseObserver.onError(Status.ALREADY_EXISTS.withDescription(
                "User with phone ${request.phone} is already exists"
            ).asRuntimeException())
            return
        }

        try {
            val user = usersService.createUser(request.phone, request.name)
            val transaction = transactionsService.requestTransactionForUser(user.uuid!!)

            responseObserver.onNext(
                TCreateUserRsp.newBuilder()
                    .setUserCreationTransaction(
                        TTransaction.newBuilder()
                            .setUuid(transaction.uuid.toString())
                            .setDeadTime(transaction.expireTimestamp.millisToTimestamp())
                            .build()
                    )
                    .build()
            )
            responseObserver.onCompleted()

            sendApproveCode(user.phone!!, transaction.code)
        } catch (e: TooOftenTransactionRequests) {
            responseObserver.onError(Status.ABORTED.withCause(e).asRuntimeException())
        }
    }

    override fun confirmUser(
        request: TConfirmUserReq,
        responseObserver: StreamObserver<TConfirmUserRsp>
    ) {
        try {
            val userUuid = transactionsService.approveTransaction(
                UUID.fromString(request.transactionUuid),
                request.confirmCode
            )

            if (userUuid == null) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Send confirmation code is invalid")
                    .asRuntimeException()
                )
                return
            }

            val now = nowProvider.nowMillis()
            responseObserver.onNext(TConfirmUserRsp.newBuilder()
                .setUserCredentials(generateCredentials(userUuid, now))
                .setConfirmationTimestamp(now.millisToTimestamp())
                .build()
            )
            responseObserver.onCompleted()
        } catch (e: TransactionNotFound) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withCause(e).asRuntimeException())
        } catch (e: TransactionExpired) {
            responseObserver.onError(Status.DEADLINE_EXCEEDED.withCause(e).asRuntimeException())
        }
    }

    override fun getUserInfo(
        request: TGetUserInfoReq,
        responseObserver: StreamObserver<TGetUserInfoRsp>
    ) {
        try {
            val jwtContent = jwtManager.extractJwtContent(request.authToken)

            if (jwtContent.tokenType != EJwtTokenType.EJwtTokenType_AUTH) {
                responseObserver.onError(
                    Status.UNAUTHENTICATED
                        .withDescription("Use auth token to get user credentials")
                        .asRuntimeException()
                )
                return
            }

            if (jwtContent.expirationTimestamp.toMillis() <= nowProvider.nowMillis()) {
                responseObserver.onError(
                    Status.UNAUTHENTICATED
                        .withDescription("Token expired")
                        .asRuntimeException()
                )
                return
            }

            val user = usersService.getUserByUUID(UUID.fromString(jwtContent.userUuid))
            if (user == null) {
                responseObserver.onError(
                    Status.UNAUTHENTICATED
                        .withDescription("User not found")
                        .asRuntimeException()
                )
                return
            }

            responseObserver.onNext(TGetUserInfoRsp.newBuilder()
                .setName(user.name)
                .setUuid(user.uuid.toString())
                .setCreationTimestamp(
                    user.creationTimestamp!!.toInstant(ZoneOffset.UTC).toEpochMilli().millisToTimestamp()
                )
                .setPhone(user.phone)
                .build()
            )
            responseObserver.onCompleted()
        } catch (e: JwtException) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid token $e")
                    .asRuntimeException()
            )
        }
    }

    override fun refreshToken(
        request: TRefreshTokenReq,
        responseObserver: StreamObserver<TRefreshTokenRsp>
    ) {
        try {
            val jwtContent = jwtManager.extractJwtContent(request.refreshToken)

            if (jwtContent.tokenType != EJwtTokenType.EJwtTokenType_REFRESH) {
                responseObserver.onError(
                    Status.UNAUTHENTICATED
                        .withDescription("Use refresh token here")
                        .asRuntimeException()
                )
                return
            }

            if (jwtContent.expirationTimestamp.toMillis() <= nowProvider.nowMillis()) {
                responseObserver.onError(
                    Status.UNAUTHENTICATED
                        .withDescription("Token expired")
                        .asRuntimeException()
                )
                return
            }

            val user = usersService.getUserByUUID(UUID.fromString(jwtContent.userUuid))
            if (user == null) {
                responseObserver.onError(
                    Status.UNAUTHENTICATED
                        .withDescription("User not found")
                        .asRuntimeException()
                )
                return
            }

            val now = nowProvider.nowMillis()
            responseObserver.onNext(TRefreshTokenRsp.newBuilder()
                .setNewCredentials(generateCredentials(user.uuid!!, now))
                .setRefreshTimestamp(now.millisToTimestamp())
                .build()
            )
            responseObserver.onCompleted()
        } catch (e: JwtException) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid token $e")
                    .asRuntimeException()
            )
        }
    }

    private fun generateCredentials(userUuid: UUID, now: Long): TCredentials {
        return TCredentials.newBuilder()
            .setAuthToken(jwtManager.createAuthToken(userUuid.toString()))
            .setAuthTokenExpirationTimestamp(
                (now + jwtManager.authTokenExistenceTime.toLong(DurationUnit.MILLISECONDS))
                    .millisToTimestamp()
            )
            .setRefreshToken(jwtManager.createRefreshToken(userUuid.toString()))
            .setRefreshTokenExpirationTimestamp(
                (now + jwtManager.refreshTokenExistenceTime.toLong(DurationUnit.MILLISECONDS))
                    .millisToTimestamp()
            )
            .build()
    }

    private fun sendApproveCode(phone: String, code: String) {
        smscClientsPool.getResource().use { smsc ->
            runBlocking (Dispatchers.IO) {
                smsc.sendMessage(
                    "Your EarnActive confirmation code is $code",
                    listOf(phone)
                )
            }
        }
    }
}
