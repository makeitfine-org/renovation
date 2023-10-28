package renovation.info.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.assertEquals
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.com.google.common.collect.ImmutableList
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile
import renovation.info.data.entity.TodoEntity

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Tag("functional")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerFunctionalTest(
    @LocalServerPort val port: Int,
) {

    companion object {
        private const val MONGO_DB_DOCKER_IMAGE = "mongo:5.0.6"
        private const val MONGO_AUTH_DB = "infodb"

        @JvmStatic
        private val OBJECT_MAPPER = ObjectMapper()

        private const val TODO_AMOUNT: Long = 5

        @Container
        val mongoDBContainer = GenericContainer(DockerImageName.parse(MONGO_DB_DOCKER_IMAGE))
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "root")
            .withEnv("MONGO_INITDB_DATABASE", MONGO_AUTH_DB)
            .withExposedPorts(27017)
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("./db/migration/mongo/init/"),
                "/docker-entrypoint-initdb.d/"
            )

        init {
            mongoDBContainer.portBindings = ImmutableList.of("0.0.0.0:" + 37017 + ":" + 27017)
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.security.enabled") { "false" }

            registry.add("spring.data.mongodb.port") { mongoDBContainer.getMappedPort(27017) }
        }
    }

    @TestConfiguration
    class ApplicationSecurity {

        @Bean
        @Throws(Exception::class)
        fun filterChain(http: HttpSecurity): SecurityFilterChain =
            http.authorizeRequests { authorizeRequests ->
                authorizeRequests.antMatchers("/**").permitAll()
                    .and().csrf().ignoringAntMatchers("/api/v1/info/todo/**")
            }.let { http.build() }
    }

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Order(0)
    @Test
    fun findAll_Success() {
        checkTodoCollectionDocumentAmount(TODO_AMOUNT)

        given()
            .When {
                get("/api/v1/info/todo")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(
                    CoreMatchers.equalTo(
                        rowJson(
                            """
                            [
                                {
                                    "id": 1,
                                    "title": "Add Casandra support",
                                    "completed": false,
                                    "date": "2023-10-18T16:10:30"
                                },
                                {
                                    "id": 2,
                                    "title": "Add git support",
                                    "completed": true,
                                    "date": "2022-03-18T15:10"
                                },
                                {
                                    "id": 3,
                                    "title": "Add firebase support",
                                    "completed": false,
                                    "date": "2023-06-11T12:10"
                                },
                                {
                                    "id": 4,
                                    "title": "Add kubernates yamls",
                                    "completed": false,
                                    "date": "2023-08-05T15:10"
                                },
                                {
                                    "id": 5,
                                    "title": "Add Postgress configs",
                                    "completed": true,
                                    "date": "2022-03-10T22:15"
                                }
                            ]
                            """
                                .trimIndent()
                        )
                    )
                )

                checkTodoCollectionDocumentAmount(TODO_AMOUNT)
            }
    }

    @Order(0)
    @Test
    fun findOne_Success() {
        checkTodoCollectionDocumentAmount(TODO_AMOUNT)

        val id = 3
        given()
            .When {
                get("/api/v1/info/todo/$id")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(
                    CoreMatchers.equalTo(
                        rowJson(
                            """
                               {
                                   "id": 3,
                                   "title": "Add firebase support",
                                   "completed": false,
                                   "date": "2023-06-11T12:10"
                               }
                            """
                                .trimIndent()
                        )
                    )
                )

                checkTodoCollectionDocumentAmount(TODO_AMOUNT)
            }
    }

    @Order(1)
    @Test
    fun update_Success() {
        checkTodoCollectionDocumentAmount(TODO_AMOUNT)

        given()
            .When {
                body(
                    """
                   {
                       "id": 3,
                       "title": "New updated",
                       "completed": false,
                       "date": "2023-08-10 10:10"
                   }
                    """.trimIndent()
                )
                    .put("/api/v1/info/todo")
            }.Then {
                statusCode(HttpStatus.SC_NO_CONTENT)

                checkTodoCollectionDocumentAmount(TODO_AMOUNT)
            }
    }

    @Order(2)
    @Test
    fun create_Success() {
        checkTodoCollectionDocumentAmount(TODO_AMOUNT)

        given()
            .When {
                body(
                    """
                   {
                       "id": 6,
                       "title": "Add firebase support (something new)",
                       "completed": true,
                       "date": "2023-07-10 10:10"
                   }
                    """.trimIndent()
                )
                    .post("/api/v1/info/todo")
            }.Then {
                statusCode(HttpStatus.SC_CREATED)

                checkTodoCollectionDocumentAmount(TODO_AMOUNT + 1)
            }
    }

    @Order(3)
    @Test
    fun delete_Success() {
        checkTodoCollectionDocumentAmount(TODO_AMOUNT + 1)

        val id = 5
        given()
            .When {
                pathParam("id", id)
                delete("/api/v1/info/todo/{id}")
            }.Then {
                statusCode(HttpStatus.SC_NO_CONTENT)

                checkTodoCollectionDocumentAmount(TODO_AMOUNT)
            }
    }

    private fun checkTodoCollectionDocumentAmount(amount: Long) = assertEquals(
        amount,
        mongoTemplate.count(Query(), TodoEntity::class.java)
    )

    private fun given() = Given {
        port(port)
            .and().header("Content-type", "application/json")
    }

    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()
}
