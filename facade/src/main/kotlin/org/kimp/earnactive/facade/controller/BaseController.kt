package org.kimp.earnactive.facade.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.Status
import io.grpc.StatusRuntimeException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.ExceptionHandler


open class BaseController {
    private val logger = KotlinLogging.logger {  }

    @ExceptionHandler(StatusRuntimeException::class)
    fun handleGrpcStubException(
        httpServletResponse: HttpServletResponse,
        grpcEx: StatusRuntimeException
    ) {
        when (grpcEx.status.code) {
            Status.Code.INVALID_ARGUMENT -> writeResponse(httpServletResponse, 400)
            Status.Code.UNAUTHENTICATED -> writeResponse(httpServletResponse, 401)
            Status.Code.NOT_FOUND -> writeResponse(httpServletResponse, 404)
            else -> {
                logger.error { "Received unhandled error: $grpcEx" }
                writeResponse(httpServletResponse, 500, "$grpcEx")
            }
        }
    }

    private fun writeResponse(
        response: HttpServletResponse,
        returnCode: Int,
        message: String = ""
    ) {
        response.status = returnCode
        response.writer.append(message)
    }
}