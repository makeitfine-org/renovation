/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import renovation.backend.data.domain.Work
import renovation.backend.data.exception.WorkNotFoundException
import renovation.backend.data.service.WorkService
import java.time.LocalDate
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(WorkController::class)
internal class WorkControllerTest(
    @Autowired
    private val mvc: MockMvc,
) {
    companion object {
        private const val API_WORK = "/api/work"
        private const val API_WORK_ID = "/api/work/{id}"
        private val OBJECT_MAPPER = ObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        private val WORKS = listOf(
            Work(
                id = 1,
                title = "title 1",
                description = "desc 1",
                price = 222.5,
                endDate = LocalDate.parse("2022-11-11"),
                payDate = LocalDate.parse("2022-10-25")
            ),
            Work(
                id = 2,
                title = "title 2",
                description = "desc 2",
                price = 5222.0,
                endDate = LocalDate.parse("2021-05-07"),
                payDate = LocalDate.parse("2022-10-15")
            ),
            Work(
                id = 3,
                title = "title 3",
                description = "desc 3",
                price = 222.5,
                endDate = LocalDate.parse("2022-11-11"),
            ),
        )
    }

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun workService() = mockk<WorkService>()
    }

    @Autowired
    private lateinit var workService: WorkService

    @BeforeEach
    fun beforeEach() {
        every { workService.findAll() } returns WORKS

        every { workService.findById(1) } returns WORKS[0]
        every { workService.findById(2) } returns WORKS[1]
        every { workService.findById(3) } returns WORKS[2]

        every { workService.findById(match { it < 1 || it > 3 }) } answers { call ->
            throw WorkNotFoundException(call.invocation.args[0] as Long)
        }

        every { workService.findByTitleLike(any()) } returns emptyList()
        every { workService.findByTitleLike("%e%2%") } returns listOf(WORKS[1])

        every { workService.update(any(), any()) } answers { call ->
            throw WorkNotFoundException(call.invocation.args[0] as Long)
        }
        every {
            workService.update(
                3,
                Work(title = "title updated", price = 500.5, endDate = LocalDate.parse("2021-05-05"))
            )
        } returns Unit

        every { workService.save(any()) } returns Unit

        every { workService.delete(any()) } answers { call ->
            throw WorkNotFoundException(call.invocation.args[0] as Long)
        }
        every {
            workService.delete(match { listOf<Long>(1, 2, 3).contains(it) })
        } returns Unit
    }

    @Test
    fun `list all`() {
        mvc.perform(get(API_WORK))
            .andExpect(status().isOk)
            .andExpect(
                content().string(
                    OBJECT_MAPPER.writeValueAsString(WORKS)
                )
            )
    }

    @Test
    fun `list all by return empty list if no works`() {
        every { workService.findAll() } returns emptyList()

        mvc.perform(get(API_WORK))
            .andExpect(status().isOk)
            .andExpect(
                content().string(
                    emptyList<String>().toString()
                )
            )
    }

    @Test
    fun `find by title like`() {
        mvc.perform(get(API_WORK).param("title", "e 2"))
            .andExpect(status().isOk)
            .andExpect(
                content().string(
                    OBJECT_MAPPER.writeValueAsString(listOf(WORKS[1]))
                )
            )
    }

    @Test
    fun `find by title like (not existence)`() {
        mvc.perform(get(API_WORK).param("title", UUID.randomUUID().toString()))
            .andExpect(status().isOk)
            .andExpect(
                content().string(
                    emptyList<String>().toString()
                )
            )
    }

    @Test
    fun `find one`() {
        val id = 3

        mvc.perform(get(API_WORK_ID, "$id"))
            .andExpect(status().isOk)
            .andExpect(
                content().string(
                    OBJECT_MAPPER.writeValueAsString(WORKS[id - 1])
                )
            )
    }

    @Test
    fun `find one (not found)`() {
        val id = Int.MIN_VALUE / 2

        mvc.perform(get(API_WORK_ID, "$id"))
            .andExpect(status().is4xxClientError)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `find one wrong id format`() {
        val id = "1 wrong"

        mvc.perform(get(API_WORK_ID, id))
            .andExpect(status().is5xxServerError)
            .andExpect(
                content().string("Internal server error (500)")
            )
    }

    @Test
    fun `update`() {
        mvc.perform(
            patch(API_WORK_ID, "3")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(
                    """
                {
                    "title": "title updated", 
                    "price": 500.5, 
                    "endDate": "2021-05-05"
                }
            """.trimIndent()
                )
        )
            .andExpect(status().isNoContent)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `update with not existence id`() {
        mvc.perform(
            patch(API_WORK_ID, "${Int.MAX_VALUE}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(
                    """
                {
                    "title": "title updated not existence id"
                }
            """.trimIndent()
                )
        )
            .andExpect(status().is4xxClientError)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `update with invalid data`() {
        mvc.perform(
            patch(API_WORK_ID, "${Int.MAX_VALUE}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(
                    """
                {
                    "price": "155abc",
                    "title": "title updated other"
                }
            """.trimIndent()
                )
        )
            .andExpect(status().is5xxServerError)
            .andExpect(
                content().string("Internal server error (500)")
            )
    }

    @Test
    fun `save`() {
        mvc.perform(
            post(API_WORK)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(
                    """
                {
                    "title": "title saved", 
                    "price": 5005.55, 
                    "endDate": "2021-10-25"
                }
            """.trimIndent()
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `save invalid input date`() {
        mvc.perform(
            post(API_WORK)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(
                    """
                {
                    "title": "title saved", 
                    "price": 5005.55, 
                    "endDate": "abc"
                }
            """.trimIndent()
                )
        )
            .andExpect(status().is5xxServerError)
            .andExpect(
                content().string("Internal server error (500)")
            )
    }

    @Test
    fun `delete`() {
        val id = 1

        mvc.perform(delete(API_WORK_ID, "$id"))
            .andExpect(status().isNoContent)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `delete with id not existence`() {
        val id = Int.MIN_VALUE - 1

        mvc.perform(delete(API_WORK_ID, "$id"))
            .andExpect(status().is4xxClientError)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `delete with invalid id format`() {
        val id = "wrong format id"

        mvc.perform(delete(API_WORK_ID, "$id"))
            .andExpect(status().is5xxServerError)
            .andExpect(
                content().string("Internal server error (500)")
            )
    }
}