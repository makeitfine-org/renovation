/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.backend.config.filter

import mu.KotlinLogging
import org.springframework.stereotype.Component
import renovation.common.util.Date.formattedNow
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest

private val log = KotlinLogging.logger { }

@Component
class LoggingFilter : HttpFilter() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        log.debug {
            "New request date: '${formattedNow()}' url: '${(request as HttpServletRequest).requestURI}'"
        }

        chain.doFilter(request, response)
    }
}
