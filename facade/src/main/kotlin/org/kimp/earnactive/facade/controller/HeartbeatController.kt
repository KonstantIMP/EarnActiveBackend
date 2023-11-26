package org.kimp.earnactive.facade.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HeartbeatController {
    @GetMapping("/ping")
    @Operation(
        summary = "Проверка доступности фасада (http api)",
        description = "В случае успеха возвращает 'pong'"
    )
    fun ping(): String = PING_ANSWER

    companion object {
        const val PING_ANSWER: String = "pong"
    }
}
