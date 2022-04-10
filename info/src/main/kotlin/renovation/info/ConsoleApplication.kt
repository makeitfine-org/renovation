/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info

import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile

@SpringBootApplication
@Profile("import")
class ConsoleApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        println("Hello!")
    }
}

fun main(args: Array<String>) {
    runApplication<ConsoleApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
