/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import dasniko.testcontainers.keycloak.KeycloakContainer
import io.restassured.module.kotlin.extensions.Given
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.testcontainers.junit.jupiter.Container

@Disabled
@Tag("functional")
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class WorkControllerWithSecurityFunctionalTest(
    @LocalServerPort val port: Int,
) : WorkControllerFunctionalTestAbstract(port) {

    companion object {

        @Container
        val keycloakContainer: KeycloakContainer = KeycloakContainer("quay.io/keycloak/keycloak:18.0.2")
            .withRealmImportFile("keycloak/renovation-realm-test.json")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            FunctionalTestAbstract.properties(registry)

            registry.add("keycloak.auth-server-url") { keycloakContainer.authServerUrl }
        }
    }

    @Value("\${keycloak.resource}")
    private lateinit var clientId: String

    @Value("\${keycloak.credentials.secret}")
    private lateinit var clientSecret: String

    @Value("\${functional-test.work.username}")
    private lateinit var workuser: String

    @Value("\${functional-test.work.password}")
    private lateinit var workpass: String

    val workToken: String
        get() = getToken(workuser, workpass)

    fun getToken(username: String, password: String): String {
        val restTemplate = RestTemplate()
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map = LinkedMultiValueMap<String, String>()
        map["grant_type"] = "password"
        map["client_id"] = clientId
        map["client_secret"] = clientSecret
        map["username"] = username
        map["password"] = password
        val token: KeyCloakToken? =
            restTemplate.postForObject(
                "${keycloakContainer.authServerUrl}/realms/renovation-realm/protocol/openid-connect/token",
                HttpEntity(
                    map,
                    httpHeaders
                ),
                KeyCloakToken::class.java
            )

        return token!!.accessToken
    }

    class KeyCloakToken {
        val accessToken: String

        @JsonCreator
        constructor(@JsonProperty("access_token") accessToken: String) {
            this.accessToken = accessToken
        }
    }

    override fun given() = Given {
        port(port)
            .and().header("Content-type", "application/json")
            .and()
            .header("Authorization", "Bearer $workToken")
    }
}
