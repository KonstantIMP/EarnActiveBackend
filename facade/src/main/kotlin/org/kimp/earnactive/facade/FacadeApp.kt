package org.kimp.earnactive.facade

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FacadeApp

fun main(args: Array<String>) {
    runApplication<FacadeApp>(*args)
}
