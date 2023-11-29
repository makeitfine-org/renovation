package renovation.ktor.server

import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.server.application.Application
import java.io.File
import mu.KotlinLogging
import renovation.ktor.server.plugins.configureRouting
import renovation.ktor.server.plugins.installs

val log = KotlinLogging.logger { }

fun main(args: Array<String>) {
    // sslConfigCreation()

    io.ktor.server.netty.EngineMain.main(args)

    log.debug { "App started!" }
}

fun sslConfigCreation() {
    val pri = "priPassword"
    val pub = "pubPassword"

    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = pri
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, pub)
}

fun Application.module() {
    installs()
    configureRouting()
}
