/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InfoApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<InfoApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
