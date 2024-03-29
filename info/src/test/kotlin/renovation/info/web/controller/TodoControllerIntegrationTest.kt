package renovation.info.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import kotlin.test.assertEquals
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Tag
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
import renovation.common.util.Rest.given
import renovation.info.data.entity.TodoEntity

@Suppress("MaxLineLength")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Tag("integrationTest")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerIntegrationTest(
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
            http.authorizeHttpRequests {
                it.requestMatchers("/**").permitAll()
            }.csrf {
                it.ignoringRequestMatchers("/api/v1/info/todo/**")
            }.let {
                http.build()
            }
    }

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Order(0)
    @Test
    fun findAll_Success() {
        checkTodoCollectionDocumentAmount(TODO_AMOUNT)

        given(port)
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
                                    "description": "Subj. Add its support as separate module with functionality or extend backend module.",
                                    "category": "IT",
                                    "image": "/assets/images/img1.jpg",
                                    "price": 0.0,
                                    "rating": {
                                        "priority": 5
                                    },
                                    "date": "2023-10-18T14:10:30"
                                },
                                {
                                    "id": 2,
                                    "title": "Buy sport products",
                                    "completed": true,
                                    "description": "Buy protein, creatine, glutamine",
                                    "category": "Other",
                                    "image": "/assets/images/img2.jpg",
                                    "price": 2000.0,
                                    "rating": null,
                                    "date": "2022-03-18T13:10"
                                },
                                {
                                    "id": 3,
                                    "title": "Improve home staff",
                                    "completed": false,
                                    "description": "Repair some things",
                                    "category": "Other",
                                    "image": null,
                                    "price": 500.5,
                                    "rating": {
                                        "priority": 3
                                    },
                                    "date": "2023-06-11T10:10"
                                },
                                {
                                    "id": 4,
                                    "title": "Add kubernates yamls",
                                    "completed": false,
                                    "description": "Improve kubernetes yaml and helm configs in project",
                                    "category": "IT",
                                    "image": "/assets/images/img4.jpg",
                                    "price": null,
                                    "rating": {
                                        "priority": 4
                                    },
                                    "date": "2023-08-05T13:10"
                                },
                                {
                                    "id": 5,
                                    "title": "Add Postgress configs",
                                    "completed": true,
                                    "description": null,
                                    "category": "IT",
                                    "image": "/assets/images/img5.jpg",
                                    "price": null,
                                    "rating": {
                                        "priority": 0
                                    },
                                    "date": "2022-03-10T20:15"
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
        given(port)
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
                                   "title": "Improve home staff",
                                   "completed": false,
                                   "description": "Repair some things",
                                   "category": "Other",
                                   "image": null,
                                   "price": 500.5,
                                   "rating": {
                                       "priority": 3
                                   },
                                   "date": "2023-06-11T10:10"
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

        given(port)
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

        given(port)
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
        given(port)
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

    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()
}
