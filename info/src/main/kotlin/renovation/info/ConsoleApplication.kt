/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConsoleApplication

fun main(args: Array<String>) {
    runApplication<ConsoleApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
