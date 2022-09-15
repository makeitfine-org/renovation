/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.config.filter

import mu.KotlinLogging
import org.springframework.security.core.context.SecurityContextHolder
import renovation.common.util.Date.formattedNow
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest

private val log = KotlinLogging.logger { }

@WebFilter(urlPatterns = ["/api/worker"])
class ApiWorkerLoggingFilter : HttpFilter() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        log.debug {
            "New request date: '${formattedNow()}' " +
                    "user: '${SecurityContextHolder.getContext().authentication.principal}' " +
                    "url: '${(request as HttpServletRequest).requestURI}'"
        }

        chain.doFilter(request, response)
    }
}
