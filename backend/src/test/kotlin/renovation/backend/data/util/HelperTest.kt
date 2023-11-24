/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.data.util

import java.time.LocalDate
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import renovation.backend.data.domain.Work
import renovation.backend.data.entity.WorkEntity

internal class HelperTest {
    companion object {
        val ID = UUID.randomUUID()
        const val TITLE = "test title"
        const val DESCRIPTION = "test description"
        val END_DATE = LocalDate.now()
        const val PRICE = 10000.0
        val PAY_DATE = LocalDate.now()
    }

    @Test
    fun `convert work entity to work`() {
        val workEntity = WorkEntity(ID, TITLE, DESCRIPTION, END_DATE, PRICE, PAY_DATE)

        val work = Helper.convert(workEntity)

        assertEquals(ID.toString(), work.id)
        assertEquals(TITLE, work.title)
        assertEquals(DESCRIPTION, work.description)
        assertEquals(END_DATE, work.endDate)
        assertEquals(PRICE, work.price)
        assertEquals(PAY_DATE, work.payDate)
    }

    @Test
    fun `convert work entity (all fields null except necessary) to work`() {
        val work = Helper.convert(WorkEntity(title = TITLE))
        assertNotNull(work.id)
    }

    @Test
    fun `convert work entity (all fields null except necessary and id = null) to work`() {
        val work = Helper.convert(WorkEntity(id = null, title = TITLE))
        assertNull(work.id)
    }

    @Test
    fun `convert work to work entity`() {
        val work = Work(ID.toString(), TITLE, DESCRIPTION, END_DATE, PRICE, PAY_DATE)

        val workEntity = Helper.convert(work)

        assertEquals(ID, workEntity.id)
        assertEquals(TITLE, workEntity.title)
        assertEquals(DESCRIPTION, workEntity.description)
        assertEquals(END_DATE, workEntity.endDate)
        assertEquals(PRICE, workEntity.price)
        assertEquals(PAY_DATE, workEntity.payDate)
    }

    @Test
    fun `convert work (all fields null) to work entity`() {
        val e = assertFailsWith<RuntimeException> {
            Helper.convert(Work())
        }
        assertEquals("title not defined", e.message)
    }
}
