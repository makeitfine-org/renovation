/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import kotlin.test.Test

/**
 * Run docker-compose up on renovation/info module before
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DetailsControllerTest(
    @LocalServerPort val port: Int
) {
    companion object {
        @JvmStatic
        private val OBJECT_MAPPER = ObjectMapper()
    }

    val portHost = "http://localhost:$port"

    @Test
    fun `getAll`() {
        When {
            get(portHost + "/api/v1/info")
        }.Then {
            statusCode(HttpStatus.SC_OK)

            val expected = """
            [
              {
                "id": "62571572b85c114c70d2c13a",
                "name": "Tom",
                "surname": "Travolta",
                "age": 27,
                "gender": "M"
              },
              {
                "id": "62571572b85c114c70d2c13b",
                "name": "Sam",
                "surname": "Berbik",
                "age": 45,
                "gender": "M"
              },
              {
                "id": "62571572b85c114c70d2c13c",
                "name": "Alfred",
                "surname": "Berbik",
                "age": 33,
                "gender": "M"
              },
              {
                "id": "62571572b85c114c70d2c13d",
                "name": "Alfred",
                "surname": "Hatton",
                "age": 33,
                "gender": "M"
              },
              {
                "id": "62571572b85c114c70d2c13e",
                "name": "Kate",
                "surname": "Hatton",
                "age": 33,
                "gender": "W"
              },
              {
                "id": "62571572b85c114c70d2c13f",
                "name": "Maestro",
                "surname": "Rave",
                "age": 27,
                "gender": "M"
              },
              {
                "id": "62571572b85c114c70d2c140",
                "name": "Kventin",
                "surname": "Toddo",
                "age": 54,
                "gender": "M"
              },
              {
                "id": "62571572b85c114c70d2c141",
                "name": "El",
                "surname": "Jey",
                "age": 18,
                "gender": "M"
              },
              {
                "id": "62571572b85c114c70d2c142",
                "name": "Anny",
                "surname": "Bally",
                "age": 18,
                "gender": "W"
              }
            ]     
            """
                .trimIndent()
            body("$.size()", Matchers.greaterThan(0))
            body(CoreMatchers.equalTo(rowJson(expected)))
        }
    }

    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()
}
