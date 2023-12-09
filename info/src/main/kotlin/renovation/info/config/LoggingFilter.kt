/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.config

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.security.core.context.SecurityContextHolder

private val log = KotlinLogging.logger { }

@WebFilter(urlPatterns = ["/*"])
class LoggingFilter : HttpFilter() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        log.debug {
            "Request | " +
                "addr: '${request.remoteAddr}' " +
                "host: '${request.remoteHost}' " +
                "port: '${request.remotePort}' " +
                "user: '${SecurityContextHolder.getContext().authentication.principal}' " +
                "url: '${(request as HttpServletRequest).requestURI}'"
        }

        chain.doFilter(request, response)
    }
}
