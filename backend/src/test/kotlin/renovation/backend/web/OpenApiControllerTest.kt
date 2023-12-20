/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web

import io.mockk.mockk
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.apache.http.HttpStatus.SC_OK
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import renovation.backend.PostgresContainerConfig
import renovation.backend.data.service.WorkService

@Tag("integrationTest")
@ContextConfiguration(
    classes = [
        PostgresContainerConfig::class,
    ]
)
@ActiveProfiles("no-security", "no-jdbc")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
internal class OpenApiControllerTest(
    @LocalServerPort val port: Int
) {

    @TestConfiguration
    class ControllerTestConfig {
        @Primary
        @Bean
        fun workService() = mockk<WorkService>()
    }

    @BeforeTest
    fun init() {
        RestAssured.port = port
    }

    @Test
    fun `openapi`() {
        When {
            get("/openapi")
        }.Then {
            statusCode(SC_OK)
        }
    }

    @Test
    fun `swagger_ui index_html`() {
        When {
            get("/swagger")
        }.Then {
            statusCode(SC_OK)
        }
    }
}
