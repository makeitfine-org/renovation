/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication(
//    exclude = [
//        SecurityAutoConfiguration::class,
//    ]
)
@EnableAsync
class TempApplication

// run before: docker-compose renovation-vault-prepopulate
@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<TempApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
