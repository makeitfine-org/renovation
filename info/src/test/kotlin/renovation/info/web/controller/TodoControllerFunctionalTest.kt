package renovation.info.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile

@Tag("functional")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerFunctionalTest(
    @LocalServerPort val port: Int,
) {

    companion object {
        private const val MONGO_DB_DOCKER_IMAGE = "mongo:5.0.6"

        @JvmStatic
        private val OBJECT_MAPPER = ObjectMapper()

        private const val WORK_COUNT: Long = 5
        private const val COUNT_WORD = "count"


        @Container
        val mongoDBContainer = GenericContainer(DockerImageName.parse(MONGO_DB_DOCKER_IMAGE))
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "root")
            .withEnv("MONGO_INITDB_DATABASE", "infodb")
            .withExposedPorts(27017)
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("./db/migration/mongo/init/v1_0__create_introuser.js"),
                "/docker-entrypoint-initdb.d/v1_0__create_introuser.js"
            )
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("./db/migration/mongo/init/v1_2__create_todos_collection.js"),
                "/docker-entrypoint-initdb.d/v1_2__create_todos_collection.js"
            )

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.security.enabled") { "false" }

            registry.add("spring.data.mongodb.uri")
            { "mongodb://root:root@localhost:${mongoDBContainer.getMappedPort(27017)}/infodb" }
        }
    }

    @TestConfiguration
    class ApplicationSecurity {

        @Bean
        @Throws(Exception::class)
        fun filterChain(http: HttpSecurity): SecurityFilterChain =
            http.authorizeRequests { authorizeRequests ->
                authorizeRequests.antMatchers("/**").permitAll()
            }.let { http.build() }
    }

    @Test
    fun findAll_Success() {
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

//                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Test
    fun findOne_Success() {
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

//                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Test
    fun create() {
    }

    @Test
    fun delete() {
    }

//    private fun checkWorkTableRecordsCountWithoutDeleted(count: Long) = assertEquals(
//        count,
//        jdbcTemplate.queryForList("select count(*) from work where deleted = false")[0][COUNT_WORD]
//    )

    protected fun given() = Given {
        port(port)
            .and().header("Content-type", "application/json")
    }

    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()
}
