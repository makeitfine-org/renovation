/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
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

// todo: fix issue with disable security
// info-6dd76885f6-lgxt2 info 2025-08-07T16:00:07.724Z DEBUG 1 --- [renovation info] [nio-9090-exec-4]
// renovation.info.config.LoggingFilter     : Log message invocation failed: java.lang.NullPointerException:
// Cannot invoke "org.springframework.security.core.Authentication.getPrincipal()" because the return value
// of "org.springframework.security.core.context.SecurityContext.getAuthentication()" is null
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
