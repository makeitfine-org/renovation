/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service

import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import renovation.common.util.Json

@Tag("integrationTest")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
internal class StartupControllerTest(
    @LocalServerPort val port: Int
) {
    @BeforeTest
    fun init() {
        RestAssured.port = port
    }

    @Test
    fun `Assert project controller`() {
        When {
            get("/module")
        }.Then {
            statusCode(SC_OK)
            body(CoreMatchers.equalTo("Hi, it's \"Renovation/kafka-service\" module"))
        }
    }

    @Test
    fun `Assert about controller`() {
        When {
            get("/about")
        }.Then {
            statusCode(SC_OK)
            body(
                CoreMatchers.equalTo(
                    Json.rowJson(
                        """
                        {
                            "name" : "renovation/kafka-service",
                            "description" : "Kafka-service module of renovation project"
                        }
                        """
                    )
                )
            )
        }
    }
}
