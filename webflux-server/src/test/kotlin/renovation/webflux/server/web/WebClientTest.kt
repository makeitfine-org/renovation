/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.webflux.server.web

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.bodyToFlow
import renovation.common.util.Json.jsonAssertEquals
import renovation.webflux.server.dto.CompanyResponse

@Tag("e2eTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebClientTest(
    @LocalServerPort val port: Int
) {
    private lateinit var webClient: WebClient

    @BeforeTest
    fun beforeAll() {
        webClient = WebClient
            .builder()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun getApiCompanyById() {
        runBlocking {
            val companyWithInfo = webClient.get()
                .uri("/api/companies/2")
                .retrieve()
                .awaitBody<CompanyResponse>()

            jsonAssertEquals(
                """
                {
                  "id": 2,
                  "name": "Company 2",
                  "address": "Address 2",
                  "users": [
                    {
                      "id": 3,
                      "email": "email-3@codersee.com",
                      "name": "Maria",
                      "age": 40
                    }
                  ]
                }
                """,
                companyWithInfo
            )
        }
    }

    @Test
    fun getApiCompanies() {
        runBlocking {
            val companies = webClient.get()
                .uri("/api/companies")
                .retrieve()
                .bodyToFlow<CompanyResponse>()

            jsonAssertEquals(
                """
                [
                  {
                    "id": 1,
                    "name": "Company 1",
                    "address": "Address 1",
                    "users": [
                      {
                        "id": 1,
                        "email": "email-1@codersee.com",
                        "name": "John",
                        "age": 23
                      },
                      {
                        "id": 2,
                        "email": "email-2@codersee.com",
                        "name": "Adam",
                        "age": 33
                      }
                    ]
                  },
                  {
                    "id": 2,
                    "name": "Company 2",
                    "address": "Address 2",
                    "users": [
                      {
                        "id": 3,
                        "email": "email-3@codersee.com",
                        "name": "Maria",
                        "age": 40
                      }
                    ]
                  },
                  {
                    "id": 3,
                    "name": "Company 3",
                    "address": "Address 3",
                    "users": [
                      {
                        "id": 4,
                        "email": "email-4@codersee.com",
                        "name": "James",
                        "age": 39
                      },
                      {
                        "id": 5,
                        "email": "email-5@codersee.com",
                        "name": "Robert",
                        "age": 41
                      },
                      {
                        "id": 6,
                        "email": "email-6@codersee.com",
                        "name": "Piotr",
                        "age": 28
                      }
                    ]
                  }
                ]
                """,
                companies.toList()
            )
        }
    }

    @Test
    fun headers() {
        runBlocking {
            val companies = webClient.get()
                .uri("/api/companies/1")
                .awaitExchange {
                    val headers = it.headers().asHttpHeaders()
                    assertEquals(MediaType.APPLICATION_JSON_VALUE, headers[HttpHeaders.CONTENT_TYPE]!![0])
                }
        }
    }
}
