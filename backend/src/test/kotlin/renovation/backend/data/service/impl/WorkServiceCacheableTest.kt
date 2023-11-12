/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.data.service.impl

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import renovation.backend.data.domain.Work
import renovation.backend.data.exception.WorkNotFoundException
import renovation.backend.data.service.WorkService
import java.util.*
import kotlin.test.*
import renovation.backend.AppByContainersConfig

@Tag("integrationTest")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class WorkServiceCacheableTest : AppByContainersConfig() {

    companion object {
        private const val cacheName = "works"
    }

    @Autowired
    @Qualifier("workServiceCacheableImpl")
    private lateinit var workService: WorkService

    @Autowired
    private lateinit var cacheManager: CacheManager

    @BeforeTest
    fun init() {
        cacheManager.getCache(cacheName)?.clear()
    }

    private fun get(uuid: UUID) = cacheManager.getCache(cacheName)?.get(uuid, Work::class.java)

    @Test
    fun `findById Cacheable if price is not greater 10000`() {
        val uuid = UUID.fromString("11111111-a845-45d7-aea9-ab624172d1c1")

        assertNull(get(uuid))
        val work = workService.findById(uuid)
        assertEquals(work, get(uuid))
    }

    @Test
    fun `findById not Cacheable if price is greater 10000`() {
        val uuid = UUID.fromString("44444444-a845-45d7-aea9-ab624172d1c1")

        assertNull(get(uuid))
        workService.findById(uuid)
        assertNull(get(uuid))
    }

    @Test
    fun `findById not Cacheable get service exception`() {
        val uuid = UUID.fromString("12345444-a845-45d7-aea9-ab624172d1c1")

        assertNull(get(uuid))
        assertFailsWith<WorkNotFoundException> {
            workService.findById(uuid)
        }
        assertNull(get(uuid))
    }

    @Test
    fun `save CachePut if price is not greater 10000`() {
        val work = workService.save(
            Work(title = "saved title ${UUID.randomUUID()}", description = "saved desc", price = 1000.5)
        )

        assertEquals(work, get(UUID.fromString(work.id)))
    }

    @Test
    fun `save CachePut if price is null`() {
        val work = workService.save(Work(title = "saved title ${UUID.randomUUID()}", description = "saved desc"))

        assertEquals(work, get(UUID.fromString(work.id)))
    }

    @Test
    fun `save not CachePut if price is greater 10000`() {
        val work = workService.save(Work(title = "saved title ${UUID.randomUUID()}", price = 15000.0))

        assertNull(get(UUID.fromString(work.id)))
    }

    @Test
    fun `save not CachePut if exception (not completed - can't get cache)`() {
        val e = assertFailsWith<RuntimeException> {
            workService.save(Work(price = 10000.0))
        }
        assertEquals("title not defined", e.message)
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    fun `update CachePut price is less than 10000 and cached before`() {
        val uuid = UUID.fromString("11111111-a845-45d7-aea9-ab624172d1c1")
        val workForUpdate =
            Work(title = "updated title ${UUID.randomUUID()}", description = "updated desc", price = 1000.5)

        assertNull(get(uuid))
        workService.findById(uuid)
        assertNotNull(get(uuid))

        assertNotEquals(workForUpdate.title, get(uuid)?.title)
        workService.update(uuid, workForUpdate)

        val workUpdatedCached = get(uuid)!!
        assertEquals(workForUpdate.title, workUpdatedCached.title)
        assertEquals(workForUpdate.description, workUpdatedCached.description)
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    fun `update CachePut price is less than 10000 and not cached before`() {
        val uuid = UUID.fromString("11111111-a845-45d7-aea9-ab624172d1c1")
        val workForUpdate =
            Work(title = "updated title ${UUID.randomUUID()}", description = "updated desc", price = 1500.0)

        assertNull(get(uuid))
        workService.update(uuid, workForUpdate)

        val workUpdatedCached = get(uuid)!!
        assertEquals(workForUpdate.title, workUpdatedCached.title)
        assertEquals(workForUpdate.description, workUpdatedCached.description)
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    fun `update CachePut price is greater than 10000 and cached before`() {
        val uuid = UUID.fromString("22222222-a845-45d7-aea9-ab624172d1c1")
        val workForUpdate =
            Work(title = "updated title ${UUID.randomUUID()}", description = "updated desc", price = 10000.5)

        assertNull(get(uuid))
        workService.findById(uuid)
        assertNotNull(get(uuid))

        assertNotEquals(workForUpdate.title, get(uuid)?.title)
        workService.update(uuid, workForUpdate)
        assertNull(get(uuid))
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    fun `update CachePut price is greater than 10000 and not cached before`() {
        val uuid = UUID.fromString("22222222-a845-45d7-aea9-ab624172d1c1")
        val workForUpdate =
            Work(title = "updated title ${UUID.randomUUID()}", description = "updated desc", price = 10000.5)

        assertNull(get(uuid))
        workService.update(uuid, workForUpdate)
        assertNull(get(uuid))
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    fun `update not CachePut if exception`() {
        val uuid = UUID.fromString("12345222-a845-45d7-aea9-ab624172d1c1")

        assertNull(get(uuid))
        assertFailsWith<WorkNotFoundException> {
            workService.update(uuid, Work(description = "desc for update ${UUID.randomUUID()}"))
        }
        assertNull(get(uuid))
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    fun `update CachePut price is less than 10000 and note cached but try before (init price more than 10000)`() {
        val uuid = UUID.fromString("44444444-a845-45d7-aea9-ab624172d1c1")
        val workForUpdate =
            Work(title = "updated title ${UUID.randomUUID()}", description = "updated desc", price = 500.0)

        assertNull(get(uuid))
        workService.findById(uuid)
        assertNull(get(uuid))

        workService.update(uuid, workForUpdate)

        val workUpdatedCached = get(uuid)!!
        assertEquals(workForUpdate.title, workUpdatedCached.title)
        assertEquals(workForUpdate.description, workUpdatedCached.description)

        // Restore pricd to 33000
        workService.update(uuid, Work(price = 33000.0))
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    fun `update CachePut price is less than 10000 and note cached before (init price more than 10000)`() {
        val uuid = UUID.fromString("44444444-a845-45d7-aea9-ab624172d1c1")
        val workForUpdate =
            Work(title = "updated title ${UUID.randomUUID()}", description = "updated desc", price = 500.0)

        assertNull(get(uuid))
        workService.update(uuid, workForUpdate)

        val workUpdatedCached = get(uuid)!!
        assertEquals(workForUpdate.title, workUpdatedCached.title)
        assertEquals(workForUpdate.description, workUpdatedCached.description)

        // Restore pricd to 33000
        workService.update(uuid, Work(price = 33000.0))
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    @Suppress("MaxLineLength")
    fun `update not CachePut price is greater than 10000 and note cached but try before (init price more than 10000)`() {
        val uuid = UUID.fromString("44444444-a845-45d7-aea9-ab624172d1c1")
        val workForUpdate =
            Work(title = "updated title ${UUID.randomUUID()}", description = "updated desc", price = 15000.5)

        assertNull(get(uuid))
        workService.findById(uuid)
        assertNull(get(uuid))

        workService.update(uuid, workForUpdate)
        assertNull(get(uuid))
    }

    @Test
    @Order(Int.MAX_VALUE - 1)
    fun `update not CachePut price is greater than 10000 and note cached before (init price more than 10000)`() {
        val uuid = UUID.fromString("44444444-a845-45d7-aea9-ab624172d1c1")
        val workForUpdate =
            Work(title = "updated title ${UUID.randomUUID()}", description = "updated desc", price = 15000.5)

        assertNull(get(uuid))
        workService.update(uuid, workForUpdate)
        assertNull(get(uuid))
    }

    @Test
    @Order(Int.MAX_VALUE)
    fun `delete CacheEvict`() {
        val uuid = UUID.fromString("11111111-a845-45d7-aea9-ab624172d1c1")

        assertNull(get(uuid))
        workService.findById(uuid)
        assertNotNull(get(uuid))
        workService.delete(uuid)
        assertNull(get(uuid))
    }

    @Test
    @Order(Int.MAX_VALUE)
    fun `delete not CacheEvict if not in cache`() {
        val uuid = UUID.fromString("44444444-a845-45d7-aea9-ab624172d1c1")

        assertNull(get(uuid))
        workService.findById(uuid)
        assertNull(get(uuid))
        workService.delete(uuid)
        assertNull(get(uuid))
    }

    @Test
    @Order(Int.MAX_VALUE)
    fun `delete not CacheEvict if exception`() {
        val uuid = UUID.fromString("12345444-a845-45d7-aea9-ab624172d1c1")

        assertNull(get(uuid))
        assertFailsWith<WorkNotFoundException> {
            workService.delete(uuid)
        }
        assertNull(get(uuid))
    }
}
