/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.info

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger { }

@Component
class CommandLineInfo(
    @Value("\${info.app.name}")
    private val appName: String,
    @Value("\${info.app.description}")
    private val appDesc: String
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        log.info("========================")
        log.info("Application name: $appName")
        log.info("Application description: $appDesc")
        log.info("========================")
    }
}
