package renovation.ktor.server

import io.ktor.server.application.Application
import mu.KotlinLogging
import renovation.ktor.server.plugins.configureRouting
import renovation.ktor.server.plugins.installs

val log = KotlinLogging.logger { }

fun main(args: Array<String>) {
    log.debug { "App started!" }
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    installs()
    configureRouting()
}
