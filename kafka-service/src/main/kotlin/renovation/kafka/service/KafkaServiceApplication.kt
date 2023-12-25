/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaServiceApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<KafkaServiceApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
