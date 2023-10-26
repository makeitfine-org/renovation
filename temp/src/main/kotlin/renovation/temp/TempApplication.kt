/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [
    SecurityAutoConfiguration::class,
])
class TempApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<TempApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}