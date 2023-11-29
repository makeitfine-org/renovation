package renovation.ktor.server.routes

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.Tag

@Tag("integrationTest")
internal class StaticContentTest {

    @Test
    fun testAbout() = testApplication {
        val response = client.get("/about")
        assertEquals(
            """
            {
                "name" : "ktor-server module",
                "description" : "Module for work with ktor"
            }
            """.trimIndent(),
            response.bodyAsText().trimIndent()
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
