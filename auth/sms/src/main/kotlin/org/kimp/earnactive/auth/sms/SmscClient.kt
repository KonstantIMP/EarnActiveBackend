package org.kimp.earnactive.auth.sms

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.appendPathSegments
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.kimp.earnactive.auth.sms.internal.Charset
import org.kimp.earnactive.auth.sms.internal.ResponseFormat
import org.kimp.earnactive.auth.sms.model.BalanceInfo
import org.kimp.earnactive.auth.sms.model.Error
import org.kimp.earnactive.auth.sms.model.Message
import org.kimp.earnactive.auth.sms.model.MessageInfo

class SmscClient (
        val login: String,
        private val password: String,
        val baseUrl: String = "https://smsc.ru"
) : SmscApi {
    private val jsonObject = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    @OptIn(InternalAPI::class)
    private val httpClient = HttpClient (CIO) {
        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, request ->
                val errorMessage = when (cause) {
                    is ClientRequestException -> parseRequestError(cause.response.content)
                    else -> cause.message ?: "Unknown error"
                }
                throw RuntimeException(errorMessage)
            }
        }

        install (ContentNegotiation) {
            json(jsonObject)
        }
    }

    override suspend fun sendMessage(
            content: String,
            phones: List<String>
    ): Message = httpClient.post(baseUrl) {
        authorizeRequest(this)
        setupCharsetAndFormat(this)

        url {
            parameters.apply {
                append("mes", content)
                append("phones", phones.joinToString(","))
            }

            appendPathSegments("sys", "send.php")
        }
    }.body()

    override suspend fun getMessageStatus(
            messageId: String,
            phones: List<String>
    ): MessageInfo = httpClient.get(baseUrl) {
        authorizeRequest(this)
        setupCharsetAndFormat(this)

        url {
            appendPathSegments("sys", "status.php")

            parameters.apply {
                append("phone", phones.joinToString(","))
                append("all", "2")
                append("id", messageId)
            }
        }
    }.body()

    override suspend fun getBalance(): BalanceInfo = httpClient.get(baseUrl) {
        authorizeRequest(this)
        setupCharsetAndFormat(this)

        url {
            appendPathSegments("sys", "balance.php")
            parameters.apply { append("cur", "1") }
        }
    }.body()

    private suspend fun parseRequestError(
        responseContent: ByteReadChannel
    ): String = responseContent.readUTF8Line()?.let {
        val errorObject = jsonObject.decodeFromString<Error>(it)
        return@let "${errorObject.error} (${errorObject.errorCode})"
    } ?: ""

    private fun authorizeRequest(requestBuilder: HttpRequestBuilder) {
        requestBuilder.url.parameters.also { p ->
            p.append("psw", password)
            p.append("login", login)
        }
    }

    private fun setupCharsetAndFormat(requestBuilder: HttpRequestBuilder) {
        requestBuilder.url.parameters.also { p ->
            p.append("charset", Charset.UTF_8.value)
            p.append("fmt", ResponseFormat.JSON.number.toString())
        }
    }
}
