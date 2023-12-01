/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.webflux.server.web

import java.time.Duration
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger { }

@RestController
class WebFluxController {

    @GetMapping("/flux_result")
    fun getResult(): Mono<String> {
        log.debug { "flux_result" }
        @Suppress("detekt:MagicNumber")
        return Mono.defer {
            Mono.just<String>(
                "Result is ready!"
            )
        }.delaySubscription(Duration.ofMillis(500))
    }
}
