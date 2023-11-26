package org.kimp.earnactive.facade.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import net.devh.boot.grpc.client.inject.GrpcClient
import org.kimp.earnactive.auth.api.IEarnActiveAuthGrpc.IEarnActiveAuthBlockingStub
import org.kimp.earnactive.auth.api.TAuthUserReq
import org.kimp.earnactive.auth.api.TConfirmUserReq
import org.kimp.earnactive.auth.api.TCreateUserReq
import org.kimp.earnactive.auth.api.TGetUserInfoReq
import org.kimp.earnactive.auth.api.TRefreshTokenReq
import org.kimp.earnactive.auth.api.TSetNameReq
import org.kimp.earnactive.facade.dto.TransactionResponse
import org.kimp.earnactive.facade.dto.UserCredentials
import org.kimp.earnactive.facade.dto.UserResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth")
class AuthController {
    @GrpcClient("auth")
    private lateinit var authStub: IEarnActiveAuthBlockingStub

    @PostMapping("/name")
    fun setName(
        @RequestHeader(name = "OAuth", required = true)
        authToken: String,
        @RequestParam("name")
        name: String
    ): UserResponse {
        authStub.setName(
            TSetNameReq.newBuilder()
                .setAccessToken(authToken)
                .setName(name)
                .build()
        )
        return getMe(authToken)
    }

    @PostMapping("/new")
    @Operation(
        summary = "Создание нового пользователя",
        description = "Запрос на авторизацию автоматически создается при создании пользователя, " +
                "то есть сразу можно переходить к /confirm.",
        parameters = [
            Parameter(
                name = "name",
                `in` = ParameterIn.QUERY,
                description = "Имя для нового пользователя",
                required = true
            ),
            Parameter(
                name = "phone",
                `in` = ParameterIn.QUERY,
                description = "Телефон пользователя (формат любой, при обработке остаются только цифры, " +
                        "7 в начале обязательна)",
                required = true
            )
        ]
    )
    fun createUser(
        @RequestParam("name")
        name: String,
        @RequestParam("phone")
        phone: String
    ): TransactionResponse {
        val transactionResponse = authStub.createUser(TCreateUserReq.newBuilder().setName(name).setPhone(phone).build())
        return TransactionResponse(transactionUuid = transactionResponse.userCreationTransaction.uuid)
    }

    @PostMapping("")
    @Operation(
        summary = "Запрос на авторизацию",
        description = "Отправляет код для авторизации пользователю",
        parameters = [
            Parameter(
                name = "phone",
                `in` = ParameterIn.QUERY,
                description = "Номер телефона пользователя",
                required = true
            )
        ]
    )
    fun auth(
        @RequestParam("phone")
        phone: String
    ): TransactionResponse {
        val transactionResponse = authStub.authUser(TAuthUserReq.newBuilder().setPhone(phone).build())
        return TransactionResponse(
            transactionUuid = transactionResponse.userAuthTransaction.uuid,
            transactionResponse.isNew
        )
    }

    @PostMapping("/confirm")
    @Operation(
        summary = "Получение токенов по коду из sms",
        parameters = [
            Parameter(
                name = "transaction",
                `in` = ParameterIn.QUERY,
                description = "uuid транзакции из /auth или /autn/new",
                required = true
            ),
            Parameter(
                name = "code",
                `in` = ParameterIn.QUERY,
                description = "Код подтверждения из sms",
                required = true
            )
        ]
    )
    fun confirm(
        @RequestParam("transaction")
        transaction: String,
        @RequestParam("code")
        code: String
    ): UserCredentials {
        val userCredentials = authStub.confirmUser(
            TConfirmUserReq.newBuilder()
                .setConfirmCode(code)
                .setTransactionUuid(transaction)
                .build()
        )
        return UserCredentials(
            authToken = userCredentials.userCredentials.authToken,
            refreshToken = userCredentials.userCredentials.refreshToken
        )
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "Обновление токенов доступа",
        description = "Выдает новую пару токенов взамен старых",
        parameters = [
            Parameter(
                name = "OAuth",
                `in` = ParameterIn.HEADER,
                description = "Refresh token для выписывания доступа",
                required = true
            )
        ]
    )
    fun refreshTokens(
        @RequestHeader(name = "OAuth", required = true)
        refreshToken: String
    ): UserCredentials {
        val credentials = authStub.refreshToken(TRefreshTokenReq.newBuilder().setRefreshToken(refreshToken).build())
        return UserCredentials(
            authToken = credentials.newCredentials.authToken,
            refreshToken = credentials.newCredentials.refreshToken
        )
    }

    @GetMapping("/me")
    @Operation(
        summary = "Возвращает информацию о пользователе",
        description = "Имя, номер телефона и uuid (возможно добавиться дата создания учетной записи)",
        parameters = [
            Parameter(
                name = "OAuth",
                `in` = ParameterIn.HEADER,
                description = "Auth token для получения доступа",
                required = true
            )
        ]
    )
    fun getMe(
        @RequestHeader(name = "OAuth", required = true)
        authToken: String
    ): UserResponse {
        val userResponse = authStub.getUserInfo(TGetUserInfoReq.newBuilder().setAuthToken(authToken).build())
        return UserResponse(
            uuid = userResponse.uuid.toString(),
            name = userResponse.name,
            phone = userResponse.phone
        )
    }
}
