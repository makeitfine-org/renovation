/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.config.filter

import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest

private val log = KotlinLogging.logger { }

@Component
class LoggingFilter : HttpFilter() {

    companion object {
        @JvmStatic
        val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss SSS")

    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        log.debug {
            "New request date: '${
                LocalDateTime.now().format(DATE_TIME_FORMAT)
            }' url: '${(request as HttpServletRequest).requestURI}'"
        }

        chain.doFilter(request, response)
    }
}
