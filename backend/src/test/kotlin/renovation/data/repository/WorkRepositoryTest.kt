/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.data.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension
import renovation.IntegrationTest
import renovation.data.entity.WorkEntity
import java.time.LocalDate

@IntegrationTest
@ExtendWith(SpringExtension::class)
@DataJpaTest
internal class WorkRepositoryTest(
    @Autowired val entityManager: TestEntityManager,
    @Autowired val workRepository: WorkRepository
) {

    companion object {

        @JvmStatic
        val WORK_RECORDS_INIT_COUNT = 5L
    }

    @Test
    fun findAll_Success() {
        val findAll = workRepository.findAll()
        assertThat(findAll.size).isEqualTo(5)
        assertTrue(
            findAll.stream().filter { w ->
                w.title.equals("title sticker")
                        && w.price?.let { it == 33000.0 }
                        ?: false
            }
                .findAny().isPresent
        )

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT)
    }

    @Test
    fun findOne_Success() {
        assertNotNull(workRepository.findById(1))

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT)
    }

    @Test
    fun save_Success() {
        val workEntity = WorkEntity(
            title = "new",
            description = "new desc",
            price = 5000.0,
            payDate = LocalDate.parse("2022-01-11"),
            endDate = LocalDate.parse("2022-01-10"),
        )
        val id = workRepository.save(workEntity).id

        val newWork = entityManager.find(WorkEntity::class.java, id)

        assertNotNull(newWork)
        assertEquals(id, newWork.id)
        assertEquals("new", newWork.title)
        assertEquals("new desc", newWork.description)
        assertEquals(5000.0, newWork.price)
        assertEquals(LocalDate.parse("2022-01-11"), newWork.payDate)
        assertEquals(LocalDate.parse("2022-01-10"), newWork.endDate)

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT + 1)
    }

    @Test
    fun update_Success() {
        val updateWorkId = 1L
        val forUpdateWork = entityManager.find(WorkEntity::class.java, updateWorkId)

        forUpdateWork.title = "new 1"
        forUpdateWork.description = "new desc 1"
        forUpdateWork.payDate = LocalDate.parse("2022-01-11")

        val id = workRepository.save(forUpdateWork).id

        val updatedWork = entityManager.find(WorkEntity::class.java, id)

        assertNotNull(updatedWork)
        assertEquals(updateWorkId, updatedWork.id)

        assertEquals("new 1", updatedWork.title)
        assertEquals("new desc 1", updatedWork.description)
        assertEquals(LocalDate.parse("2022-01-11"), updatedWork.payDate)

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT)
    }

    @Test
    fun delete_Success() {
        val deleteWorkId = 3L
        workRepository.deleteById(deleteWorkId)

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT - 1)
    }

    private fun workRecordsExceptedCount(exceptedCount: Long) = assertEquals(
        exceptedCount,
        entityManager.entityManager.createQuery(
            "select count(work) from WorkEntity work"
        ).resultList[0]
    )
}