/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.ktor.server.data.services

import renovation.ktor.server.data.models.Book

@Suppress("detekt:MagicNumber")
class BookService {
    private val books = mutableListOf<Book>()

    init {
        books.add(Book(1, "C Programming Language", "Kernighan Brian W", "Pearson"))
        books.add(
            Book(
                2,
                "Eloquent JavaScript, 3rd Edition: A Modern Introduction to Programming",
                "Marijn Haverbeke",
                "No Starch Press"
            )
        )
        books.add(Book(3, "Clean Code", "Robert C Martin", "Pearson"))
        books.add(
            Book(
                4,
                "Python Crash Course, 2nd Edition: A Hands-On, Project-Based Introduction to Programming",
                "Eric Matthes",
                "No Starch Press"
            )
        )
        books.add(Book(5, "Effective Java", "Joshua Bloch", "Addison-Wesley Professional"))
        books.add(Book(6, "Introduction to Algorithms: CLRS", "Thomson H.Cormen", "PHI"))
    }

    fun getBooks(): List<Book> = books

    fun getBook(id: Int?): Book? {
        return books.find { book -> book.id == id }
    }

    fun addBook(book: Book): Book? {
        books.add(book)
        return book
    }

    fun deleteBook(id: Int?) {
        books.removeAll { book -> book.id == id }
    }
}
