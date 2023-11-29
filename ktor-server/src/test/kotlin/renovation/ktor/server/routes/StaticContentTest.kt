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
    fun testLogPng() = testApplication {
        val response = client.get("/static/ktor_logo.png")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testLogPngNotExists() = testApplication {
        val response = client.get("/static/ktor_logo.pngNo")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAboutMeHtml() = testApplication {
        val response = client.get("/static/about.me.html")
        assertEquals(
            """
            <!--
              ~ Created under not commercial project "Renovation"
              ~
              ~ Copyright 2021-2023
              -->

            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Kotlin Journal</title>
            </head>
            <body style="text-align: center; font-family: sans-serif">
            <img src="/static/ktor_logo.png" alt="ktor logo">
            <h1>About me</h1>
            <div>
                <p>Welcome to my static page!</p>
                <p>Feel free to take a look around.</p>
                <p>Or go to the <a href="/static/about.me.html">main page</a>.</p>
            </div>
            </body>
            </html>
            """.trimIndent(),
            response.bodyAsText().trimIndent()
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
