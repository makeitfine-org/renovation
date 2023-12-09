/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info

import jakarta.annotation.PostConstruct
import java.util.TimeZone
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan
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
