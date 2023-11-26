package org.kimp.earnactive.facade.controller

import io.grpc.Status
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.ExceptionHandler

open class BaseController {

    @ExceptionHandler(Exception::class)
    fun handleGrpcStubException(
        httpServletResponse: HttpServletResponse,
        grpcEx: Exception
    ) {
        val status = Status.fromThrowable(grpcEx)
        when (status) {
            Status.NOT_FOUND -> writeResponse(httpServletResponse, 404)
            Status.UNAUTHENTICATED -> writeResponse(httpServletResponse, 401)
            Status.INVALID_ARGUMENT -> writeResponse(httpServletResponse, 400)
            Status.ALREADY_EXISTS -> writeResponse(httpServletResponse, 403)
        }
    }

    private fun writeResponse(
        response: HttpServletResponse,
        returnCode: Int,
        message: String = ""
    ) {
        response.status = returnCode
        response.writer.also {
            print(message)
        }
    }
}