/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.webflux.server.config

import java.time.Duration
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger { }

@Component
class WebFluxFilter : WebFilter {
    override fun filter(serverWebExchange: ServerWebExchange, webFilterChain: WebFilterChain): Mono<Void> {
        log.debug { "doFilter" }
        @Suppress("detekt:MagicNumber")
        return Mono
            .delay(Duration.ofMillis(200))
            .then(
                webFilterChain.filter(serverWebExchange)
            )
    }
}
