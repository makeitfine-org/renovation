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
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import renovation.common.util.Json
import renovation.common.web.Client

private val log = KotlinLogging.logger { }

@Component
@Endpoint(id = "keycloakhealth")
class KeycloakHealthEndpoint(
    @Value("\${KEYCLOAK_HOSTNAME:localhost}")
    private val host: String,

    @Value("\${KEYCLOAK_PORT2:18080}")
    private val port: String,
) {
    @ReadOperation
    fun check(): String {
        try {
            val response = Client.RestClient.getForObject(
                "http://$host:$port/health",
                String::class.java
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
