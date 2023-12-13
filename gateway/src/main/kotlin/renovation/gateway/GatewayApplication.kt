/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatewayApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
