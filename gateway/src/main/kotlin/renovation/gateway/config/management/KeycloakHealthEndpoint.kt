/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway.config.management

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import renovation.common.util.Json
import renovation.common.web.Client

private val log = KotlinLogging.logger { }

@Component
@Endpoint(id = "keycloakhealth")
@Profile("!social-login & !certificate")
class KeycloakHealthEndpoint(
    @Value("\${keycloak.auth-server-url}")
    val keycloakUrl: String,
) {
    @ReadOperation
    fun check(): Any {
        try {
            val response = Client.RestClient.getForObject(
                "$keycloakUrl/health",
                Any::class.java
            )
            return response!!
        } catch (e: RestClientException) {
            log.error { e }
            return Json.OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object {
                val status = "Down"
            })
        }
    }
}
