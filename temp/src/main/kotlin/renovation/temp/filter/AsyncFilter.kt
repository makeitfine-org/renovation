/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.filter

import java.io.IOException
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger { }

@Component
internal class AsyncFilter : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        // sleep for 200ms
        @Suppress("detekt:MagicNumber")
        Thread.sleep(200)
        log.debug { "doFilter" }
        filterChain.doFilter(servletRequest, servletResponse)
    }
}
