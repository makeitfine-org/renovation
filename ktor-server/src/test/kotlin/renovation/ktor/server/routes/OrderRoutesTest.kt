package renovation.ktor.server.routes

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.Tag
import renovation.common.util.Json.rowJson

@Tag("integrationTest")
internal class OrderRoutesTest {

    @Test
    fun testGetOrder() = testApplication {
        val response = client.get("/order/2020-04-06-01")
        assertEquals(
            rowJson(
                """
                {
                  "number": "2020-04-06-01",
                  "contents": [
                    {
                      "item": "Ham Sandwich",
                      "amount": 2,
                      "price": 5.5
                    },
                    {
                      "item": "Water",
                      "amount": 1,
                      "price": 1.5
                    },
                    {
                      "item": "Beer",
                      "amount": 3,
                      "price": 2.3
                    },
                    {
                      "item": "Cheesecake",
                      "amount": 1,
                      "price": 3.75
                    }
                  ]
                }
                """.trimIndent()
            ),
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testAllOrder() = testApplication {
        val response = client.get("/order")
        assertEquals(
            rowJson(
                """
            [
              {
                "number": "2020-04-06-01",
                "contents": [
                  {
                    "item": "Ham Sandwich",
                    "amount": 2,
                    "price": 5.5
                  },
                  {
                    "item": "Water",
                    "amount": 1,
                    "price": 1.5
                  },
                  {
                    "item": "Beer",
                    "amount": 3,
                    "price": 2.3
                  },
                  {
                    "item": "Cheesecake",
                    "amount": 1,
                    "price": 3.75
                  }
                ]
              },
              {
                "number": "2020-04-03-01",
                "contents": [
                  {
                    "item": "Cheeseburger",
                    "amount": 1,
                    "price": 8.5
                  },
                  {
                    "item": "Water",
                    "amount": 2,
                    "price": 1.5
                  },
                  {
                    "item": "Coke",
                    "amount": 2,
                    "price": 1.76
                  },
                  {
                    "item": "Ice Cream",
                    "amount": 1,
                    "price": 2.35
                  }
                ]
              }
            ]
                """.trimIndent()
            ),
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
