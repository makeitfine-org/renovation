/*
 *
 *  * Created under project "Bina"
 *  *
 *  * Copyright 2022
 *
 */

package renovation.gateway

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
