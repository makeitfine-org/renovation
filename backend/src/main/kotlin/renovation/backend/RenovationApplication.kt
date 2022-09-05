/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RenovationApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<RenovationApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
