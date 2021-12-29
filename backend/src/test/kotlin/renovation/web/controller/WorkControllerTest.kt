/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.web.controller

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.commons.io.FileUtils
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.util.ResourceUtils
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import renovation.web.interceptor.GlobalControllerExceptionHandler.Companion.INTERNAL_SERVER_ERROR
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.sql.Date
import java.time.LocalDate

@Testcontainers
@Tag("integration")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WorkControllerTest(
    @LocalServerPort val port: Int,
    @Autowired val jdbcTemplate: JdbcTemplate
) {
    private val FILL_WORK_TABLE_SCRIPT = FileUtils.readFileToString(
        ResourceUtils.getFile(
            "classpath:db/liquibase/changelogs/create_and_fill_work_table/sql/fill_work_table.sql"
        ),
        StandardCharsets.UTF_8
    )

    companion object {
        private const val WORK_COUNT: Long = 5
        private const val COUNT_WORD = "count"

        @Container
        private val container: PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>("postgres:13.3-alpine")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.username", container::getUsername);
        }
    }

    @BeforeEach
    fun fillWorkTable() {
        jdbcTemplate.execute("truncate work restart identity")
        jdbcTemplate.execute(FILL_WORK_TABLE_SCRIPT)
    }

    @Test
    fun `container should be run`() {
        jdbcTemplate.isIgnoreWarnings
        assertTrue(container.isRunning)

        val url = URL("http://${container.containerIpAddress}:${container.firstMappedPort}")

        url.openConnection() as HttpURLConnection
    }

    @Test
    fun findAllWorks_Success() {
        given()
            .When {
                get("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(CoreMatchers.equalTo("[{\"title\":\"air condition installation\",\"desc\":\"\",\"endDate\":\"2021-10-15\",\"price\":2500.0,\"payDate\":\"2021-10-15\"},{\"title\":\"pipe installation\",\"desc\":\"Andery from Bila Cerkva did it\",\"endDate\":\"2021-10-25\",\"price\":8000.0,\"payDate\":\"2021-10-30\"},{\"title\":\"plaster\",\"desc\":\"Vasyl did it\",\"endDate\":\"2021-11-10\",\"price\":null,\"payDate\":null},{\"title\":\"tile sticker\",\"desc\":\"\",\"endDate\":\"2021-12-01\",\"price\":33000.0,\"payDate\":\"2021-12-05\"},{\"title\":\"electrical wiring\",\"desc\":\"\",\"endDate\":\"2021-12-10\",\"price\":8000.0,\"payDate\":null}]"))

                checkWorkTableRecordsCount(WORK_COUNT)
            }
    }

    @Test
    fun findOneWork_Success() {
        val workId = 4
        given()
            .When {
                get("/api/work/${workId}")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(CoreMatchers.equalTo("{\"title\":\"tile sticker\",\"desc\":\"\",\"endDate\":\"2021-12-01\",\"price\":33000.0,\"payDate\":\"2021-12-05\"}"))

                checkWorkTableRecordsCount(WORK_COUNT)
            }
    }

    @Test
    fun findOneWork_EmptyList_Success() {
        given()
            .When {
                jdbcTemplate.execute("delete from work")

                get("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(CoreMatchers.equalTo("[]"))

                checkWorkTableRecordsCount(0)
            }
    }

    @Test
    fun findOneWork_Return404IfWorkNotFound_Fail() {
        val workId = Int.MAX_VALUE
        given()
            .When {
                get("/api/work/${workId}")
            }.Then {
                statusCode(HttpStatus.SC_NOT_FOUND)
            }
    }

    @Test
    fun findOneWork_Return500IfWorkIdFormatIsInvalid_Fail() {
        val workId = "invalidFormatId"
        given()
            .When {
                get("/api/work/${workId}")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))
            }
    }

    @Test
    fun createWork_Success() {
        given()
            .When {
                body("{\"title\":\"title it\",\"desc\": \"desc it\", \"price\": -773.3, \"payDate\": \"2020-11-28\" }")
                post("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_CREATED)

                checkWorkTableRecordsCount(WORK_COUNT + 1)
            }
    }

    @Test
    fun createWork_Return500IfDataFormatIsInvalid_Fail() {
        val invalidFormatPrice = "773.3a";
        given()
            .When {
                body("{\"title\":\"title it\",\"desc\": \"desc it\", \"price\": $invalidFormatPrice, \"payDate\": \"2020-11-28\" }")
                post("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCount(WORK_COUNT)
            }
    }

    @Test
    fun updateWork_Success() {
        val workId = 1
        given()
            .When {
                body("{\"title\":\"title update\",\"desc\": \"desc update\", \"price\": 773.31, \"payDate\": \"2020-11-18\" }")
                patch("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_NO_CONTENT)

                checkWorkTableRecordsCount(WORK_COUNT)

                val res = jdbcTemplate.queryForList("select * from work where id=$workId")[0]
                assertEquals(1L, res["id"])
                assertEquals("title update", res["title"])
                assertEquals("desc update", res["desc"])
                assertEquals(LocalDate.parse("2020-11-18"), (res["pay_date"] as Date).toLocalDate())
            }
    }

    @Test
    fun updateWork_Return404IfWorkNotFound_Fail() {
        val workId = Int.MAX_VALUE
        given()
            .When {
                body("{\"title\":\"title update\",\"desc\": \"desc update\", \"price\": 773.31, \"payDate\": \"2020-11-18\" }")
                patch("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_NOT_FOUND)

                checkWorkTableRecordsCount(WORK_COUNT)
            }
    }

    @Test
    fun updateWork_Return500IfWorkDataFormatIsInvalid_Fail() {
        val workId = 3
        val invalidPayDate = "2020-111-1"
        given()
            .When {
                body("{\"title\":\"title update\",\"desc\": \"desc update\", \"price\": 773.31, \"payDate\": \"$invalidPayDate\" }")
                patch("/api/work/${workId}")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCount(WORK_COUNT)
            }
    }

    @Test
    fun deleteWork_Success() {
        val workId = 2
        given()
            .When {
                delete("/api/work/${workId}")
            }.Then {
                statusCode(HttpStatus.SC_NO_CONTENT)

                checkWorkTableRecordsCount(WORK_COUNT - 1)
            }
    }

    @Test
    fun deleteWork_Return404IfWorkNotFound_Fail() {
        val workId = Int.MAX_VALUE
        given()
            .When {
                delete("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_NOT_FOUND)

                checkWorkTableRecordsCount(WORK_COUNT)
            }
    }

    @Test
    fun delete_Return500IfWorkIdFormatIsInvalid_Fail() {
        val workId = "3a"
        given()
            .When {
                delete("/api/work/${workId}")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCount(WORK_COUNT)
            }
    }

    private fun checkWorkTableRecordsCount(count: Long) {
        assertEquals(
            count,
            jdbcTemplate.queryForList("select count(*) from work")[0][COUNT_WORD]
        )
    }

    private fun given() = Given {
        port(port)
            .and().header("Content-type", "application/json")
    }
}
