/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.util

import renovation.backend.data.domain.Work
import renovation.backend.data.entity.WorkEntity
import java.time.LocalDate
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class HelperTest {
    companion object {
        val id = UUID.randomUUID()
        const val title = "test title"
        const val description = "test description"
        val endDate = LocalDate.now()
        const val price = 10000.0
        val payDate = LocalDate.now()
    }

    @Test
    fun `convert work entity to work`() {
        val workEntity = WorkEntity(id, title, description, endDate, price, payDate)

        val work = Helper.convert(workEntity)

        assertEquals(id.toString(), work.id)
        assertEquals(title, work.title)
        assertEquals(description, work.description)
        assertEquals(endDate, work.endDate)
        assertEquals(price, work.price)
        assertEquals(payDate, work.payDate)
    }

    @Test
    fun `convert work entity (all fields null except necessary) to work`() {
        val work = Helper.convert(WorkEntity(title = title))
        assertNotNull(work.id)
    }

    @Test
    fun `convert work entity (all fields null except necessary and id = null) to work`() {
        val work = Helper.convert(WorkEntity(id = null, title = title))
        assertNull(work.id)
    }

    @Test
    fun `convert work to work entity`() {
        val work = Work(id.toString(), title, description, endDate, price, payDate)

        val workEntity = Helper.convert(work)

        assertEquals(id, workEntity.id)
        assertEquals(title, workEntity.title)
        assertEquals(description, workEntity.description)
        assertEquals(endDate, workEntity.endDate)
        assertEquals(price, workEntity.price)
        assertEquals(payDate, workEntity.payDate)
    }

    @Test
    fun `convert work (all fields null) to work entity`() {
        val e = assertFailsWith<RuntimeException> {
            Helper.convert(Work())
        }
        assertEquals("title must be defined", e.message)
    }
}
