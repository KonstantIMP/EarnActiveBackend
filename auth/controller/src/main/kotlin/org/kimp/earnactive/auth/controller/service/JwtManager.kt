package org.kimp.earnactive.auth.controller.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.kimp.earnactive.auth.api.EJwtTokenType
import org.kimp.earnactive.auth.api.TJwtContent
import org.kimp.earnactive.common.now.NowProvider
import org.kimp.earnactive.common.now.SystemNowProvider
import org.kimp.earnactive.utils.millisToTimestamp
import kotlin.time.Duration
import kotlin.time.DurationUnit

class JwtManager (
    private val secret: String,
    val authTokenExistenceTime: Duration,
    val refreshTokenExistenceTime: Duration,
    private val nowProvider: NowProvider = SystemNowProvider()
) {
    fun extractJwtContent(jwt: String): TJwtContent = TJwtContent.parseFrom(
        Jwts.parser().setSigningKey(secret).build()
            .parseContentJws(jwt)
            .payload
    )

    fun createAuthToken(
        userUuid: String
    ) : String = createToken(userUuid, EJwtTokenType.EJwtTokenType_AUTH)

    fun createRefreshToken(
        userUuid: String
    ) : String = createToken(userUuid, EJwtTokenType.EJwtTokenType_REFRESH)

    private fun createToken(
        uuid: String,
        type: EJwtTokenType
    ) : String {
        val expirationMillis = nowProvider.nowMillis() + getExistenceMillis(type)
        val jwtContent = TJwtContent.newBuilder().apply {
            expirationTimestamp = expirationMillis.millisToTimestamp()
            userUuid = uuid
            tokenType = type
        }.build()

        return Jwts.builder()
            .content(jwtContent.toByteArray(), "application/protobuf")
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    private fun getExistenceMillis(type: EJwtTokenType) = when (type) {
        EJwtTokenType.EJwtTokenType_AUTH -> authTokenExistenceTime
        EJwtTokenType.EJwtTokenType_REFRESH -> refreshTokenExistenceTime
        else -> throw RuntimeException("Unknown jwt token type: $type")
    }.toLong(DurationUnit.MILLISECONDS)
}
