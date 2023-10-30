/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info

import java.util.TimeZone
import javax.annotation.PostConstruct
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class InfoApplication {

    @PostConstruct
    fun init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
}

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<InfoApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
