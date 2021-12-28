/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.web.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import renovation.data.domain.Work
import renovation.data.entity.WorkEntity
import renovation.data.exception.WorkNotFoundException
import renovation.data.service.WorkService
import renovation.data.util.Helper.Companion.convert

@RestController
@RequestMapping("/api/work")
class WorkController(@Autowired val workService: WorkService) {

    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(WorkController::class.java)
    }

    @GetMapping
    fun list(): List<Work> {
        LOG.info("find all works")
        return workService.findAll().stream().map { e -> convert(e) }.toList()
    }

    @GetMapping("{id}")
    fun find(@PathVariable id: Long): Work {
        LOG.info("find work by id: ${id}")
        return workService.findById(id)?.let { convert(it) } ?: throw WorkNotFoundException(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody work: Work) {
        val workEntity = convert(work)
        LOG.info("create work: ${workEntity}")
        workService.save(workEntity)
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Long, @RequestBody work: Work) {
        val workEntityForUpdate = workService.findById(id)?.let { it } ?: throw WorkNotFoundException(id)
        updateEntity(work, workEntityForUpdate)
        LOG.info("udpate work with id = ${id} work: ${workEntityForUpdate}")
        workService.save(workEntityForUpdate)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        if (!workService.existsById(id))
            throw WorkNotFoundException(id)

        workService.delete(id)
        LOG.info("delete work with id = ${id}")
    }

    private fun updateEntity(work: Work, workEntityForUpdate: WorkEntity) {
        work.title?.let { workEntityForUpdate.title = it }
        work.desc?.let { workEntityForUpdate.desc = it }
        work.endDate?.let { workEntityForUpdate.endDate = it }
        work.price?.let { workEntityForUpdate.price = it }
        work.payDate?.let { workEntityForUpdate.payDate = it }
    }
}
