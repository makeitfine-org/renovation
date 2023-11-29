package renovation.ktor.server.routes

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.Tag

@Tag("integrationTest")
internal class CustomerRoutesTest {

    @Test
    fun testGetCustomer() = testApplication {
        val response = client.get("/customer")
        assertEquals("No customers found", response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
