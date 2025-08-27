/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.backend.data.service.impl

import java.time.LocalDate
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Tag
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.never
import org.mockito.Mockito.only
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.cache.CacheManager
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import renovation.backend.PostgresContainerConfig
import renovation.backend.RedisContainerConfig
import renovation.backend.data.domain.Work
import renovation.backend.data.exception.WorkNotFoundException
import renovation.backend.data.repository.WorkRepository
import renovation.backend.data.service.WorkService

@Tag("integrationTest")
@ActiveProfiles("no-security")
@SpringBootTest
@ContextConfiguration(
    classes = [
        PostgresContainerConfig::class,
        RedisContainerConfig::class,
    ]
)
@Transactional
internal class WorkServiceCacheTest {

    @Autowired
    @Qualifier("workServiceCacheableImpl")
    private lateinit var workService: WorkService

    @SpyBean
    private lateinit var workRepository: WorkRepository

    @Autowired
    private lateinit var cacheManager: CacheManager

    @BeforeTest
    fun init() {
        cacheManager.getCache(WorkServiceCacheableImpl.CACHE_WORK_BY_ID)?.clear()
        cacheManager.getCache(WorkServiceCacheableImpl.CACHE_WORKS_ALL)?.clear()
    }

    @Test
    fun `get find all works`() {
        verify(workRepository, never()).findAll(any(Sort::class.java))

        var foundAll = workService.findAll()
        verify(workRepository, only()).findAll(any(Sort::class.java))
        assertNotNull(foundAll)

        assertEquals(workService.findAll(), foundAll) // second call
        assertEquals(workService.findAll(), foundAll) // third call
        verify(workRepository, only()).findAll(any(Sort::class.java))
    }

    @Test
    fun `get work by id twice with suitable criteria caching fine`() {
        val uuid = UUID.fromString("11111111-a845-45d7-aea9-ab624172d1c1")

        verify(workRepository, never()).findById(uuid)

        var foundById = workService.findById(uuid)
        verify(workRepository, only()).findById(uuid)
        assertNotNull(foundById)

        assertEquals(workService.findById(uuid), foundById)
        verify(workRepository, only()).findById(uuid)
    }

    @Test
    fun `get work by id twice with suitable criteria NO fine`() {
        val uuid = UUID.fromString("44444444-a845-45d7-aea9-ab624172d1c1")

        var foundById = workService.findById(uuid)
        verify(workRepository, only()).findById(uuid)
        assertNotNull(foundById)

        assertEquals(workService.findById(uuid), foundById)
        verify(workRepository, times(2)).findById(uuid)
    }

    @Test
    fun `save work by id`() {
        var savedId = workService.save(
            Work(
                title = "new title",
                description = "saved desc",
                endDate = LocalDate.parse("2023-11-11"),
                price = 1012.5,
                payDate = LocalDate.parse("2023-11-13")
            )
        ).id.let { UUID.fromString(it) }

        workService.findById(savedId)
        workService.findById(savedId) // double call

        verify(workRepository, never()).findById(savedId)
    }

    @Test
    fun `update work by id`() {
        val uuid = UUID.fromString("11111111-a845-45d7-aea9-ab624172d1c1")

        var work = workService.findById(uuid)
        workService.findById(uuid)
        verify(workRepository, only()).findById(uuid)

        var updatedWork = workService.update(uuid, work.copy(title = "updated title")) // inside findById call
        verify(workRepository, times(2)).findById(uuid) // was inside call
        assertEquals("updated title", updatedWork.title)
        workService.findById(uuid) // double call
        workService.findById(uuid) // triple call
        verify(workRepository, times(2)).findById(uuid)

        updatedWork = workService.update(uuid, work.copy(title = "updated title 2"))
        verify(workRepository, times(3)).findById(uuid)
        assertEquals("updated title 2", updatedWork.title)
        workService.findById(uuid) // double call
        workService.findById(uuid) // triple call
        verify(workRepository, times(3)).findById(uuid)

        // no such id
        val e = assertFailsWith<WorkNotFoundException> {
            workService.update(
                UUID.fromString("db74a2b4-52ba-43f7-ab8d-4eb50616a8ce"),
                work.copy(title = "updated title 2")
            )
        }
        assertEquals("Work with id: db74a2b4-52ba-43f7-ab8d-4eb50616a8ce not found", e.message)

        workService.findById(uuid) // fourth call
        verify(workRepository, times(3)).findById(uuid)
    }

    @Test
    fun `delete work by id`() {
        val uuid = UUID.fromString("44444444-a845-45d7-aea9-ab624172d1c1")

        workService.delete(uuid)
        verify(workRepository, never()).findById(uuid)

        assertFailsWith<WorkNotFoundException> {
            workService.findById(uuid)
        }
        verify(workRepository, times(1)).findById(uuid)
    }
}
