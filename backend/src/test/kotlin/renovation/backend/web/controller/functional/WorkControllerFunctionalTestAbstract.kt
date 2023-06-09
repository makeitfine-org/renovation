/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import renovation.backend.web.interceptor.GlobalControllerExceptionHandler.Companion.INTERNAL_SERVER_ERROR
import java.net.HttpURLConnection
import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import renovation.backend.FunctionalTestAbstract

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal abstract class WorkControllerFunctionalTestAbstract(
    private val port: Int,
) : FunctionalTestAbstract() {

    companion object {
        @JvmStatic
        private val OBJECT_MAPPER = ObjectMapper()

        private const val WORK_COUNT: Long = 5
        private const val COUNT_WORD = "count"
    }

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Order(0)
    @Test
    fun `container should be run`() {
        jdbcTemplate.isIgnoreWarnings
        assertTrue(postgresContainer.isRunning)
        assertTrue(redisContainer.isRunning)

        val url = URL("http://${postgresContainer.host}:${postgresContainer.firstMappedPort}")

        url.openConnection() as HttpURLConnection
    }

    @Order(1)
    @Test
    fun findAllWorks_Success() {
        given()
            .When {
                get("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                // todo: think of float with .0 is not reduce in restassured and reduce in spring mvc
                body(
                    CoreMatchers.equalTo(
                        rowJson(
                            """
                    [
                       {
                          "id":"11111111-a845-45d7-aea9-ab624172d1c1",
                          "title":"air condition installation",
                          "description":"",
                          "endDate":"2021-10-15",
                          "price":2500.0,
                          "payDate":"2021-10-15"
                       },
                       {
                          "id":"22222222-a845-45d7-aea9-ab624172d1c1",
                          "title":"pipe installation",
                          "description":"Andery from Bila Cerkva did it",
                          "endDate":"2021-10-25",
                          "price":8000.0,
                          "payDate":"2021-10-30"
                       },
                       {
                          "id":"33333333-a845-45d7-aea9-ab624172d1c1",
                          "title":"plaster",
                          "description":"Vasyl did it",
                          "endDate":"2021-11-10",
                          "price":null,
                          "payDate":null
                       },
                       {
                          "id":"44444444-a845-45d7-aea9-ab624172d1c1",
                          "title":"title sticker",
                          "description":"",
                          "endDate":"2021-12-01",
                          "price":33000.0,
                          "payDate":"2021-12-05"
                       },
                       {
                          "id":"55555555-a845-45d7-aea9-ab624172d1c1",
                          "title":"electrical wiring",
                          "description":"",
                          "endDate":"2021-12-10",
                          "price":8000.0,
                          "payDate":null
                       }
                    ]
                """
                                .trimIndent()
                        )
                    )
                )

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(Int.MAX_VALUE)
    @Test
    fun findAllWork_EmptyList_Success() {
        given()
            .When {
                jdbcTemplate.execute("delete from work")

                get("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(CoreMatchers.equalTo("[]"))

                checkWorkTableRecordsCountWithoutDeleted(0)
            }
    }

    @Order(1)
    @Test
    fun findOneWork_Success() {
        val workId = "44444444-a845-45d7-aea9-ab624172d1c1"
        given()
            .When {
                pathParam("id", workId)
                get("/api/work/{id}")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(
                    CoreMatchers.equalTo(
                        rowJson(
                            """
                    {
                       "id":"44444444-a845-45d7-aea9-ab624172d1c1",
                       "title":"title sticker",
                       "description":"",
                       "endDate":"2021-12-01",
                       "price":33000.0,
                       "payDate":"2021-12-05"
                    }
                """
                                .trimIndent()
                        )
                    )
                )

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(1)
    @Test
    fun findOneWork_Return404IfWorkNotFound_Fail() {
        val workId = "bca11111-a845-45d7-aea9-ab624172d1c1"
        given()
            .When {
                pathParam("id", workId)
                get("/api/work/{id}")
            }.Then {
                statusCode(HttpStatus.SC_NOT_FOUND)
            }
    }

    @Order(1)
    @Test
    fun findOneWork_Return500IfWorkIdFormatIsInvalid_Fail() {
        val workId = "invalidFormatId"
        given()
            .When {
                get("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))
            }
    }

    @Order(1)
    @Test
    fun findByTitleLike_Success() {
        val title = "ti"
        given()
            .When {
                queryParam("title", title)
                    .get("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(
                    CoreMatchers.equalTo(
                        rowJson(
                            """
                    [
                       {
                          "id":"11111111-a845-45d7-aea9-ab624172d1c1",
                          "title":"air condition installation",
                          "description":"",
                          "endDate":"2021-10-15",
                          "price":2500.0,
                          "payDate":"2021-10-15"
                       },
                       {
                          "id":"22222222-a845-45d7-aea9-ab624172d1c1",
                          "title":"pipe installation",
                          "description":"Andery from Bila Cerkva did it",
                          "endDate":"2021-10-25",
                          "price":8000.0,
                          "payDate":"2021-10-30"
                       },
                       {
                          "id":"44444444-a845-45d7-aea9-ab624172d1c1",
                          "title":"title sticker",
                          "description":"",
                          "endDate":"2021-12-01",
                          "price":33000.0,
                          "payDate":"2021-12-05"
                       }
                    ]    
                            """.trimIndent()
                        )
                    )
                )

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(1)
    @Test
    fun findByTitleLike_EmptyList_Success() {
        val title = UUID.randomUUID().toString()
        given()
            .When {
                queryParam("title", title)
                    .get("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(CoreMatchers.equalTo("[]"))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(2)
    @Test
    fun updateWork_Success() {
        val workId = "11111111-a845-45d7-aea9-ab624172d1c1"
        given()
            .When {
                body(
                    """
                    {
                       "title":"title update",
                       "description":"desc update",
                       "price":773.31,
                       "payDate":"2020-11-18"
                    }    
                    """.trimIndent()
                )
                    .pathParam("id", workId)
                    .patch("/api/work/{id}")
            }.Then {
                statusCode(HttpStatus.SC_NO_CONTENT)

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)

                val res = jdbcTemplate
                    .queryForList("select * from work where id='11111111-a845-45d7-aea9-ab624172d1c1'")[0]
                assertEquals(UUID.fromString(workId), res["id"])
                assertEquals("title update", res["title"])
                assertEquals("desc update", res["description"])
                assertEquals(LocalDate.parse("2020-11-18"), (res["pay_date"] as Date).toLocalDate())
            }
    }

    @Order(2)
    @Test
    fun updateWork_Return404IfWorkNotFound_Fail() {
        val workId = "dab33333-a845-45d7-aea9-ab624172d1c1"
        given()
            .When {
                body(
                    """
                    {
                       "title":"title update",
                       "description":"desc update",
                       "price":773.31,
                       "payDate":"2020-11-18"
                    }
                    """.trimIndent()
                )
                    .pathParam("id", workId)
                    .patch("/api/work/{id}")
            }.Then {
                statusCode(HttpStatus.SC_NOT_FOUND)

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(2)
    @Test
    fun updateWork_Return500IfWorkDataFormatIsInvalid_Fail() {
        val workId = 3
        val invalidPayDate = "2020-111-1"
        given()
            .When {
                body(
                    """
                    {
                       "title":"title update",
                       "description":"desc update",
                       "price":773.31,
                       "payDate":"$invalidPayDate"
                    }
                    """.trimIndent()
                )
                patch("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(2)
    @Test
    fun updateWork_Return500If4SpacesSymbolsTitle_Fail() {
        val workId = 3
        val invalidPayDate = "2020-11-01"
        given()
            .When {
                body(
                    """
                    {
                       "title":"    ",
                       "description":"desc update",
                       "price":773.31,
                       "payDate":"$invalidPayDate"
                    }
                    """.trimIndent()
                )
                patch("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(2)
    @Test
    fun updateWork_Return500If2SymbolsTitle_Fail() {
        val workId = 3
        val invalidPayDate = "2020-11-01"
        given()
            .When {
                body(
                    """
                    {
                       "title":"ab",
                       "description":"desc update",
                       "price":773.31,
                       "payDate":"$invalidPayDate"
                    }
                    """.trimIndent()
                )
                patch("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }


    @Order(30)
    @Test
    fun createWork_Success() {
        given()
            .When {
                body(
                    """
                    {
                       "title":"title it",
                       "description":"desc it",
                       "price":-773.3,
                       "payDate":"2020-11-28"
                    }
                    """.trimIndent()
                )
                    .post("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_CREATED)

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT + 1)
            }
    }

    @Order(31)
    @Test
    fun createWork_Return500IfDataFormatIsInvalid_Fail() {
        val invalidFormatPrice = "773.3a"
        given()
            .When {
                body(
                    """
                    {
                       "title":"title it",
                       "description":"desc it",
                       "price":"$invalidFormatPrice",
                       "payDate":"2020-11-28"
                    }
                    """.trimIndent()
                )
                post("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT + 1)
            }
    }

    @Order(31)
    @Test
    fun createWork_Return500IfDataHasNoTitle_Fail() {
        given()
            .When {
                body(
                    """
                    {
                       "description":"desc it",
                       "price":10000.0,
                       "payDate":"2020-11-28"
                    }
                    """.trimIndent()
                )
                post("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT + 1)
            }
    }

    @Order(31)
    @Test
    fun createWork_Return500If4SpacesSymbolsTitle_Fail() {
        given()
            .When {
                body(
                    """
                    {
                       "title":"    ",
                       "description":"desc it",
                       "price":10000.0,
                       "payDate":"2020-11-28"
                    }
                    """.trimIndent()
                )
                post("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT + 1)
            }
    }

    @Order(31)
    @Test
    fun createWork_Return500IfTwoSymbolsTitle_Fail() {
        given()
            .When {
                body(
                    """
                    {
                       "title":"ab",
                       "description":"desc it",
                       "price":10000.0,
                       "payDate":"2020-11-28"
                    }
                    """.trimIndent()
                )
                post("/api/work")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT + 1)
            }
    }

    @Order(40)
    @Test
    fun deleteWork_Success() {
        checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT + 1)

        val workId = "22222222-a845-45d7-aea9-ab624172d1c1"

        val deletedSql = "select deleted from work where id = '$workId'"

        assertFalse {
            jdbcTemplate.queryForList(deletedSql)[0]["DELETED"] as Boolean
        }

        given()
            .When {
                pathParam("id", workId)
                delete("/api/work/{id}")
            }.Then {
                statusCode(HttpStatus.SC_NO_CONTENT)

                assertTrue {
                    jdbcTemplate.queryForList(deletedSql)[0]["DELETED"] as Boolean
                }

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(41)
    @Test
    fun deleteWork_Return404IfWorkNotFound_Fail() {
        val workId = "bcc33333-a845-45d7-aea9-ab624172d1c1"
        given()
            .When {
                delete("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_NOT_FOUND)

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    @Order(41)
    @Test
    fun delete_Return500IfWorkIdFormatIsInvalid_Fail() {
        val workId = "3a"
        given()
            .When {
                delete("/api/work/$workId")
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                body(CoreMatchers.equalTo(INTERNAL_SERVER_ERROR))

                checkWorkTableRecordsCountWithoutDeleted(WORK_COUNT)
            }
    }

    private fun checkWorkTableRecordsCountWithoutDeleted(count: Long) = assertEquals(
        count,
        jdbcTemplate.queryForList("select count(*) from work where deleted = false")[0][COUNT_WORD]
    )

    protected open fun given() = Given {
        port(port)
            .and().header("Content-type", "application/json")
    }

    // todo: move to util module (shred between modules to use)
    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()
}
