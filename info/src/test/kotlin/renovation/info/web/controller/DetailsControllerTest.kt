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
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import kotlin.test.Test

/**
 * Run docker-compose up on renovation/info module before
 */
@Tag("smoke")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DetailsControllerTest(
    @LocalServerPort private val port: Int
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
              {
                "id": "62571572b85c114c70d2c101",
                "name": "Tom",
                "surname": "Travolta",
                "age": 27,
                "gender": "Male",
                "detailsEmails": [
                  {
                    "email": "tt27@email.one",
                    "emailStatus": "Active"
                  }
                ],
                "additionInfos": [
                  {
                    "nickName": "Doro One",
                    "phoneNumber": "+389229222123"
                  },
                  {
                    "nickName": "Doro Two",
                    "phoneNumber": "+389229222323"
                  }
                ]
              },
              {
                "id": "62571572b85c114c70d2c102",
                "name": "Sam",
                "surname": "Berbik",
                "age": 45,
                "gender": "Male",
                "detailsEmails": null,
                "additionInfos": null
              },
              {
                "id": "62571572b85c114c70d2c103",
                "name": "Alfred",
                "surname": "Berbik",
                "age": 33,
                "gender": "Male",
                "detailsEmails": null,
                "additionInfos": null
              },
              {
                "id": "62571572b85c114c70d2c104",
                "name": "Alfred",
                "surname": "Hatton",
                "age": 33,
                "gender": "Male",
                "detailsEmails": null,
                "additionInfos": null
              },
              {
                "id": "62571572b85c114c70d2c105",
                "name": "Kate",
                "surname": "Hatton",
                "age": 33,
                "gender": "Female",
                "detailsEmails": [
                  {
                    "email": "kh33@email.com",
                    "emailStatus": "Active"
                  },
                  {
                    "email": "kh33_other@email.two",
                    "emailStatus": "Inactive"
                  }
                ],
                "additionInfos": null
              },
              {
                "id": "62571572b85c114c70d2c106",
                "name": "Maestro",
                "surname": "Rave",
                "age": 27,
                "gender": "Male",
                "detailsEmails": null,
                "additionInfos": null
              },
              {
                "id": "62571572b85c114c70d2c107",
                "name": "Kventin",
                "surname": "Toddo",
                "age": 54,
                "gender": "Male",
                "detailsEmails": [
                  {
                    "email": "kt54_other@email.two",
                    "emailStatus": "Closed"
                  }
                ],
                "additionInfos": null
              },
              {
                "id": "62571572b85c114c70d2c108",
                "name": "El",
                "surname": "Jey",
                "age": 18,
                "gender": "Male",
                "detailsEmails": null,
                "additionInfos": [
                  {
                    "nickName": "quacky",
                    "phoneNumber": "+389229229123"
                  }
                ]
              },
              {
                "id": "62571572b85c114c70d2c109",
                "name": "Anny",
                "surname": "Bally",
                "age": 18,
                "gender": "Female",
                "detailsEmails": null,
                "additionInfos": null
              }
            """
                .trimIndent()
            body("$.size()", Matchers.greaterThan(0))
//            body(Matchers.matchesPattern(".*"))
            body(CoreMatchers.containsString(rowJson(expected)))
        }
    }

    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()
}
