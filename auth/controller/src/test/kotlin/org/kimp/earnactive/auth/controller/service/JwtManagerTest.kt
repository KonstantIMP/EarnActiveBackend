package org.kimp.earnactive.auth.controller.service

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.kimp.earnactive.auth.api.EJwtTokenType
import org.kimp.earnactive.common.now.FakeNowProvider
import org.kimp.earnactive.utils.toMillis
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.DurationUnit

class JwtManagerTest {
    private lateinit var jwtManager: JwtManager

    @BeforeTest
    fun createBaseJwtManager() {
        jwtManager = JwtManager(
            DEFAULT_SECRET,
            shortDuration,
            longDuration,
            FakeNowProvider(0)
        )
    }

    @Test
    fun jwtGenerationTest() {
        jwtManager.createAuthToken("uuid")
        jwtManager.createRefreshToken("uuid")
    }

    @Test
    fun jwtContentExtractionTest() {
        val jwtContent = jwtManager.extractJwtContent(
            jwtManager.createAuthToken("uuid")
        )

        assertThat(jwtContent.userUuid, equalTo("uuid"))
        assertThat(jwtContent.tokenType, equalTo(EJwtTokenType.EJwtTokenType_AUTH))
        assertThat(jwtContent.expirationTimestamp.toMillis(), equalTo(shortDuration.toLong(DurationUnit.MILLISECONDS)))
    }

    companion object {
        val shortDuration = Duration.parse("10s")
        val longDuration = Duration.parse("20s")

        const val DEFAULT_SECRET = "ljNmkBsK0lmPud273CrvYIJ" +
                                   "/QFdR4lPNJpDBQpILOva1L6" +
                                   "WEibPIbRxDTeG1Z8KVRCjjI" +
                                   "OqbpEeovk4kWLcP/7aEGZD5" +
                                   "dofAC6dW/aIDV/B/pHUM0a0" +
                                   "lRrItcJIBjX4y2DtMpn5fuc" +
                                   "r4NX/YCC/MFPhlbXfAvDUVQ" +
                                   "oE9R8eoNjlA8K1swoawZklF" +
                                   "zFk/soCf3KWx57NEK2xxQyp" +
                                   "la3jh6loCxZAM1Uq0Xx8f1w=="
    }
}
