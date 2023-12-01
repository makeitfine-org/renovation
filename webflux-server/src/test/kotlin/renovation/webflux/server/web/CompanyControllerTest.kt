package renovation.webflux.server.web

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import renovation.common.util.Json
import renovation.common.util.Rest.given

@Tag("e2eTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanyControllerTest(
    @LocalServerPort val port: Int
) {
    @Test
    fun getApiCompanies() {
        given(port)
            .When {
                get("/api/companies")
            }.Then {
                body(
                    CoreMatchers.equalTo(
                        Json.rowJson(
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
                            """.trimIndent()
                        )
                    )
                )
            }
    }

    @Test
    fun getApiCompaniesByName() {
        given(port)
            .When {
                queryParam("name", "Company 3")
                get("/api/companies")
            }.Then {
                body(
                    CoreMatchers.equalTo(
                        Json.rowJson(
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
                            """.trimIndent()
                        )
                    )
                )
            }
    }

    @Test
    fun getApiCompanyById() {
        given(port)
            .When {
                pathParams("id", "2")
                get("/api/companies/{id}")
            }.Then {
                body(
                    CoreMatchers.equalTo(
                        Json.rowJson(
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
                            """.trimIndent()
                        )
                    )
                )
            }
    }
}
