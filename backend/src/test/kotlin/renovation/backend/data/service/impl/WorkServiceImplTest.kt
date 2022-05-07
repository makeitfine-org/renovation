/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.service.impl

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Sort
import renovation.backend.TestHelper
import renovation.backend.data.domain.Work
import renovation.backend.data.entity.WorkEntity
import renovation.backend.data.exception.WorkNotFoundException
import renovation.backend.data.repository.WorkRepository
import renovation.backend.data.util.Helper
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class)
internal class WorkServiceImplTest {

    companion object {
        @JvmStatic
        private val WORK_MAP: Map<UUID, Work> = TestHelper.WORKS.associateBy {
            UUID.fromString(it.id)
        }

        @JvmStatic
        private val WORK_ENTITIES_MAP: Map<UUID, WorkEntity> = WORK_MAP.entries.associate {
            it.key to Helper.convert(it.value)
        }
    }

    @MockK
    private lateinit var workRepository: WorkRepository

    @InjectMockKs
    private lateinit var workService: WorkServiceImpl

    @BeforeTest
    fun init() {
        every {
            workRepository.findAll()
        } returns WORK_ENTITIES_MAP.values.toList().sortedBy { it.id }
        every {
            workRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
        } returns WORK_ENTITIES_MAP.values.toList().sortedBy { it.id }

        every { workRepository.findById(any()) } returns Optional.empty()
        every {
            workRepository.findById(
                match {
                    WORK_ENTITIES_MAP.containsKey(it)
                }
            )
        } answers {
            Optional.ofNullable(WORK_ENTITIES_MAP[call.invocation.args[0] as UUID])
        }

        every { workRepository.findByTitleLike(any()) } returns emptyList()
        every {
            workRepository.findByTitleLike("%e%2%")
        } returns listOf(WORK_ENTITIES_MAP[UUID.fromString("22222222-05da-40d7-9781-aad518619682")]!!)

        every {
            workRepository.save(any())
        } answers {
            val entity = call.invocation.args[0] as WorkEntity
            entity.id = UUID.randomUUID()
            entity
        }

        every {
            workRepository.existsById(any())
        } answers {
            WORK_ENTITIES_MAP.containsKey(call.invocation.args[0])
        }

        every { workRepository.deleteById(any()) } returns Unit

        every { workRepository.findById(isNull()) } throws IllegalArgumentException()
        every { workRepository.existsById(isNull()) } throws IllegalArgumentException()
        every { workRepository.deleteById(isNull()) } throws IllegalArgumentException()
    }

    @Test
    fun `findAll`() {
        assertEquals(TestHelper.WORKS, workService.findAll())
    }

    @Test
    fun `findById`() {
        val id = UUID.fromString("11111111-05da-40d7-9781-aad518619682")

        assertEquals(WORK_MAP[id], workService.findById(id))
    }

    @Test
    fun `findById not found id (failed)`() {
        val idString = "12345111-05da-40d7-9781-aad518619682"

        val e = assertFailsWith<WorkNotFoundException> {
            workService.findById(UUID.fromString(idString))
        }

        assertEquals("Work with id: 12345111-05da-40d7-9781-aad518619682 not found", e.message)
    }

    @Test
    fun `find by title like`() {
        assertEquals(
            listOf(WORK_MAP[UUID.fromString("22222222-05da-40d7-9781-aad518619682")]),
            workService.findByTitleLike("%e%2%")
        )
    }

    @Test
    fun `find by title like (no associates)`() {
        Assertions.assertThat(
            workService.findByTitleLike(UUID.randomUUID().toString())
        ).isEmpty()
    }

    @Test
    fun `save`() {
        assertDoesNotThrow<Exception> {
            workService.save(Work(title = "any other"))
            return
        }
    }

    @Test
    fun `save without title set (failed)`() {
        val e = assertFailsWith<RuntimeException> {
            workService.save(Work(description = "any desc"))
        }

        assertEquals("title must be defined", e.message)
    }

    @Test
    fun `update`() {
        assertDoesNotThrow<Exception> {
            workService.update(
                UUID.fromString("22222222-05da-40d7-9781-aad518619682"),
                Work(title = "any other")
            )
            return
        }
    }

    @Test
    fun `update with not found id (failed)`() {
        assertFailsWith<WorkNotFoundException> {
            workService.update(
                UUID.fromString("12345222-05da-40d7-9781-aad518619682"),
                Work(title = "any other new title")
            )
        }
    }

    @Test
    fun `delete`() {
        assertDoesNotThrow<Exception> {
            workService.delete(
                UUID.fromString("33333333-05da-40d7-9781-aad518619682"),
            )
            return
        }
    }

    @Test
    fun `delete with not found id (failed)`() {
        assertFailsWith<WorkNotFoundException> {
            workService.delete(
                UUID.fromString("12345333-05da-40d7-9781-aad518619682"),
            )
        }
    }
}
