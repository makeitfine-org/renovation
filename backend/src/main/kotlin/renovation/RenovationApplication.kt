/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RenovationApplication

fun main(args: Array<String>) {
    runApplication<RenovationApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
