/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.micrometer.core.instrument.Counter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import renovation.backend.TestHelper.WORKS
import renovation.backend.data.domain.Work
import renovation.backend.data.exception.WorkNotFoundException
import renovation.backend.data.service.WorkService
import java.time.LocalDate
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExtendWith(SpringExtension::class)
@WebMvcTest(WorkController::class)
@ContextConfiguration(classes = [WorkControllerTestAbstract.ControllerTestConfig::class])
@Suppress("UnnecessaryAbstractClass")
internal abstract class WorkControllerTestAbstract {

    companion object {
        const val API_WORK = "/api/work"
        const val API_WORK_ID = "/api/work/{id}"
        val OBJECT_MAPPER = ObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @TestConfiguration
    class ControllerTestConfig {
        @Primary
        @Bean("workServiceCacheableImpl")
        fun workService() = mockk<WorkService>()

        @Primary
        @Bean
        fun getAllWorksCounter() = mockk<Counter>()
    }

    @Autowired
    protected lateinit var mvc: MockMvc

    @Autowired
    protected lateinit var workService: WorkService

    @Autowired
    protected lateinit var getAllWorksCounter: Counter

    protected open fun patch(urlTemplate: String, vararg uriVars: Any?) =
        MockMvcRequestBuilders.patch(urlTemplate, *uriVars)

    protected open fun post(urlTemplate: String, vararg uriVars: Any?) =
        MockMvcRequestBuilders.post(urlTemplate, *uriVars)

    protected open fun delete(urlTemplate: String, vararg uriVars: Any?) =
        MockMvcRequestBuilders.delete(urlTemplate, *uriVars)

    @BeforeTest
    @Suppress("ThrowsCount")
    fun beforeEach() {
        every { workService.findAll() } returns WORKS

        every { workService.findById(UUID.fromString("11111111-05da-40d7-9781-aad518619682")) } returns WORKS[0]
        every { workService.findById(UUID.fromString("22222222-05da-40d7-9781-aad518619682")) } returns WORKS[1]
        every { workService.findById(UUID.fromString("33333333-05da-40d7-9781-aad518619682")) } returns WORKS[2]

        every {
            workService.findById(
                match {
                    !WORKS.stream().map { e -> UUID.fromString(e.id) }.anyMatch { e -> it == e }
                }
            )
        } answers { call ->
            throw WorkNotFoundException(call.invocation.args[0] as UUID)
        }

        every { workService.findByTitleLike(any()) } returns emptyList()
        every { workService.findByTitleLike("%e%2%") } returns listOf(WORKS[1])

        every { workService.update(any(), any()) } answers { call ->
            throw WorkNotFoundException(call.invocation.args[0] as UUID)
        }
        every {
            workService.update(
                UUID.fromString("33333333-05da-40d7-9781-aad518619682"),
                Work(title = "title updated", price = 500.5, endDate = LocalDate.parse("2021-05-05"))
            )
        } answers {
            val updatedId = (call.invocation.args[0] as UUID).toString()
            (call.invocation.args[1] as Work).copy(id = updatedId)
        }

        every {
            workService.save(any())
        } answers {
            (call.invocation.args[0] as Work).copy(id = UUID.randomUUID().toString())
        }

        every { workService.save(any()) } returns Work()

        every { workService.delete(any()) } answers { call ->
            throw WorkNotFoundException(call.invocation.args[0] as UUID)
        }
        every {
            workService.delete(match { WORKS.stream().map { e -> UUID.fromString(e.id) }.anyMatch { e -> it == e } })
        } returns Unit

        every { getAllWorksCounter.increment() } returns Unit
    }

    @Test
    fun `list all`() {
        mvc.perform(get(API_WORK))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().string(
                    OBJECT_MAPPER.writeValueAsString(WORKS)
                )
            )
    }

    @Test
    fun `list all by return empty list if no works`() {
        every { workService.findAll() } returns emptyList()

        mvc.perform(get(API_WORK))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().string(
                    emptyList<String>().toString()
                )
            )
    }

    @Test
    fun `find by title like`() {
        mvc.perform(get(API_WORK).param("title", "e 2"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().string(
                    OBJECT_MAPPER.writeValueAsString(listOf(WORKS[1]))
                )
            )
    }

    @Test
    fun `find by title like (not existence)`() {
        mvc.perform(get(API_WORK).param("title", UUID.randomUUID().toString()))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().string(
                    emptyList<String>().toString()
                )
            )
    }

    @Test
    fun `find one`() {
        val id = WORKS[2].id

        mvc.perform(get(API_WORK_ID, "$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().string(
                    OBJECT_MAPPER.writeValueAsString(WORKS[2])
                )
            )
    }

    @Test
    fun `find one (not found)`() {
        val id = "abc33333-05da-40aa-9a81-aad518619682"

        mvc.perform(get(API_WORK_ID, "$id"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    @Test
    fun `find one wrong id format`() {
        val id = "1 wrong"

        mvc.perform(get(API_WORK_ID, id))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError)
            .andExpect(
                MockMvcResultMatchers.content().string("Internal server error (500)")
            )
    }

    @Test
    fun `update`() {
        mvc.perform(
            this.patch(API_WORK_ID, WORKS[2].id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                    "title": "title updated", 
                    "price": 500.5, 
                    "endDate": "2021-05-05"
                }
            """
                        .trimIndent()
                )
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    @Test
    fun `update with not existence id`() {
        mvc.perform(
            this.patch(API_WORK_ID, "dbc33333-05da-40a7-9781-aad518619682")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                    "title": "title updated not existence id"
                }
                    """
                        .trimIndent()
                )
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    @Test
    fun `update with invalid data`() {
        mvc.perform(
            this.patch(API_WORK_ID, "${Int.MAX_VALUE}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                    "price": "155abc",
                    "title": "title updated other"
                }
                    """.trimIndent()
                )
        )
            .andExpect(MockMvcResultMatchers.status().is5xxServerError)
            .andExpect(
                MockMvcResultMatchers.content().string("Internal server error (500)")
            )
    }

    @Test
    fun `save`() {
        mvc.perform(
            this.post(API_WORK)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
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
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    @Test
    fun `save invalid input date`() {
        mvc.perform(
            this.post(API_WORK)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
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
            .andExpect(MockMvcResultMatchers.status().is5xxServerError)
            .andExpect(
                MockMvcResultMatchers.content().string("Internal server error (500)")
            )
    }

    @Test
    fun `delete`() {
        val id = WORKS[0].id

        mvc.perform(
            this.delete(API_WORK_ID, "$id"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    @Test
    fun `delete with id not existence`() {
        val id = "ccc3333a-17db-40d7-9781-aad518619699"

        mvc.perform(
            this.delete(API_WORK_ID, "$id"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    @Test
    fun `delete with invalid id format`() {
        val id = "wrong format id"

        mvc.perform(
            this.delete(API_WORK_ID, "$id"))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError)
            .andExpect(
                MockMvcResultMatchers.content().string("Internal server error (500)")
            )
    }
}
