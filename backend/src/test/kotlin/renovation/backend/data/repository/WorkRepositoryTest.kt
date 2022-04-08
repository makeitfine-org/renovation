/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.repository

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.FilterType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import renovation.backend.IntegrationTest
import renovation.backend.config.PersistenceConfig
import renovation.backend.data.entity.WorkEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@IntegrationTest
@ExtendWith(SpringExtension::class)
@DataJpaTest(
    includeFilters = [
        Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [PersistenceConfig::class]
        )
    ]
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class WorkRepositoryTest(
    @Autowired private val entityManager: TestEntityManager,
    @Autowired private val workRepository: WorkRepository,
    @Autowired private val jdbcTemplate: JdbcTemplate
) {
    companion object {
        @JvmStatic
        val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")

        @JvmStatic
        val WORK_RECORDS_INIT_COUNT = 5L
    }

    @Test
    fun findAll_Success() {
        val findAll = workRepository.findAll()
        assertEquals(5, findAll.size)
        assertTrue {
            findAll.stream().filter { w ->
                w.title.equals("title sticker") &&
                    w.price?.let { it == 33000.0 }
                        ?: false
            }
                .findAny().isPresent
        }

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT)
    }

    @Test
    fun findOne_Success() {
        assertNotNull(workRepository.findById(UUID.fromString("11111111-05da-40d7-9781-aad518619682")))

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT)
    }

    @Test
    fun findOne_WrongIdFormat_Fail() {
        val e = assertFailsWith<NumberFormatException> {
            workRepository.findById(UUID.fromString("m1111111-05da-40d7-9781-aad518619682"))
        }
        assertEquals("Error at index 0 in: \"m1111111\"", e.message)
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

        assertEquals(LocalDateTime.now().format(DATE_TIME_FORMAT), newWork.createdDate?.format(DATE_TIME_FORMAT))
        assertEquals(LocalDateTime.now().format(DATE_TIME_FORMAT), newWork.lastModifiedDate?.format(DATE_TIME_FORMAT))

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT + 1)
    }

    @Test
    fun update_Success() {
        val updateWorkId = UUID.fromString("11111111-a845-45d7-aea9-ab624172d1c1")
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

        assertNull(updatedWork.createdDate)
        assertNull(updatedWork.lastModifiedDate)

        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT)
    }

    @Test
    fun delete_Success() {
        val deleteWorkId = UUID.fromString("33333333-a845-45d7-aea9-ab624172d1c1")
        val deletedSql = "select deleted from work where id = '$deleteWorkId'"

        assertFalse {
            jdbcTemplate.queryForList(deletedSql)[0]["DELETED"] as Boolean
        }

        workRepository.deleteById(deleteWorkId)
        workRepository.flush()

        assertTrue {
            jdbcTemplate.queryForList(deletedSql)[0]["DELETED"] as Boolean
        }
        assertEquals(5L, 5)
        assertEquals(
            WORK_RECORDS_INIT_COUNT,
            jdbcTemplate.queryForList("select count(*) from work")[0]["count(*)"] as Long
        )
        workRecordsExceptedCount(WORK_RECORDS_INIT_COUNT - 1)
    }

    private fun workRecordsExceptedCount(exceptedCount: Long) = assertEquals(
        exceptedCount,
        entityManager.entityManager.createQuery(
            "select count(work) from WorkEntity work"
        ).resultList[0]
    )
}
