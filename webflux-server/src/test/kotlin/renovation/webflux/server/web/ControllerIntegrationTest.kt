package renovation.webflux.server.web

import kotlin.test.Test
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import renovation.common.util.Json.rowJson

@Tag("integrationTest")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerIntegrationTest(
    @LocalServerPort val port: Int,
) {
    companion object {
        private const val MYSQL_DOCKER_IMAGE = "mysql:8.0"

        @Container
        val mysqlContainer: MySQLContainer<Nothing> = MySQLContainer<Nothing>(MYSQL_DOCKER_IMAGE)
            .withDatabaseName("r2")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") {
                "r2dbc:mysql://" +
                    mysqlContainer.host + ":" + mysqlContainer.firstMappedPort +
                    "/" + mysqlContainer.databaseName
            }
            registry.add("spring.r2dbc.username") { mysqlContainer.password }
            registry.add("spring.r2dbc.password") { mysqlContainer.username }

            registry.add("spring.flyway.url") { mysqlContainer.jdbcUrl }
        }
    }

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun saveOrderTest() {
        webTestClient.get()
            .uri("/about")
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(
                rowJson(
                    """
                {
                    "name" : "renovation webflux server module",
                    "description" : "Webflux part of renovation project"
                }
                """
                )
            )
    }

    @Test
    fun getApiCompanyById() {
        webTestClient.get()
            .uri("/api/companies/2")
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(
                rowJson(
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
                    """
                )
            )
    }

    @Test
    fun searchByNamesTest() {
        webTestClient.get()
            .uri("/api/search?query=pa")
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(
                rowJson(
                    """
                    [
                      {
                        "id": 1,
                        "name": "Company 1",
                        "type": "COMPANY"
                      },
                      {
                        "id": 2,
                        "name": "Company 2",
                        "type": "COMPANY"
                      },
                      {
                        "id": 3,
                        "name": "Company 3",
                        "type": "COMPANY"
                      }
                    ]
                    """
                )
            )
    }

    @Test
    fun getApiCompaniesByName() {
        webTestClient.get()
            .uri("/api/companies?name=Company 3")
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(
                rowJson(
                    """
                    [
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
                    """
                )
            )
    }
}
