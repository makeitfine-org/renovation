package renovation.ktor.server.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import renovation.ktor.server.data.models.Book
import renovation.ktor.server.data.services.BookService

fun Route.bookRouting() {

    val bookService = BookService()

    route("/book") {
        get {
            call.respond(bookService.getBooks())
        }
        get("/{id}") {
            val bookIdFromQuery = call.parameters["id"] ?: fail("Please provide a valid id")
            val book = bookService.getBook(bookIdFromQuery?.toIntOrNull())
            if (book == null) {
                call.respond(HttpStatusCode.NotFound, "Book not found");
            } else {
                call.respond(book)
            }
        }
        post {
            val requestBody = call.receive<Book>()
            bookService.addBook(requestBody)
            call.respond(requestBody)
        }
        delete("/{id}") {
            val bookIdFromQuery = call.parameters["id"] ?: fail("Please provide a valid id")
            bookService.deleteBook(bookIdFromQuery?.toIntOrNull())
            call.respond("Book is deleted")
        }
    }
}

private fun fail(message: String): Nothing = throw Exception()
