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
class MaestroApplication

fun main(args: Array<String>) {
    runApplication<MaestroApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
