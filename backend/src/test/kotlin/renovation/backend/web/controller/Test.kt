package renovation.backend.web.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

//internal class Test : WorkControllerFunctionalTestBase(
//    @LocalServerPort
//    private val port: Int
//) {
//
//    @Value("\${keycloak.resource}")
//    private lateinit var clientId: String
//
//    @Value("\${keycloak.credentials.secret}")
//    private lateinit var clientSecret: String
//
//    @Value("\${functional-test.work.username}")
//    private lateinit var workuser: String
//
//    @Value("\${functional-test.work.password}")
//    private lateinit var workpass: String
//
//    val workToken: String
//        get() = getToken(workuser, workpass)
//
//    fun getToken(username: String, password: String): String {
//        val restTemplate = RestTemplate()
//        val httpHeaders = HttpHeaders()
//        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
//
//        val map = LinkedMultiValueMap<String, String>()
//        map["grant_type"] = "password"
//        map["client_id"] = clientId
//        map["client_secret"] = clientSecret
//        map["username"] = username
//        map["password"] = password
//        val token: KeyCloakToken? =
//            restTemplate.postForObject(
//                "${keycloakContainer.authServerUrl}/realms/renovation-realm/protocol/openid-connect/token",
//                HttpEntity(
//                    map,
//                    httpHeaders
//                ),
//                KeyCloakToken::class.java
//            )
//
//        return token!!.accessToken
//    }
//
//    class KeyCloakToken {
//        val accessToken: String
//
//        @JsonCreator
//        constructor(@JsonProperty("access_token") accessToken: String) {
//            this.accessToken = accessToken
//        }
//    }
//}

//.and()
//.header("Authorization", "Bearer $workToken")
