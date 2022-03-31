/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import renovation.backend.data.domain.Work
import renovation.backend.data.service.WorkService

@CrossOrigin(originPatterns = ["http://localhost:80*"])
@RestController
@RequestMapping("/api/work")
class WorkController(private val workService: WorkService) {

    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(WorkController::class.java)
    }

    @GetMapping
    fun list(@RequestParam title: String?): List<Work> {
        LOG.info("find all works")
        return if (title.isNullOrBlank())
            workService.findAll()
        else {
            val titleLikePattern = "%" + title.replace(" ", "%") + "%"
            workService.findByTitleLike(titleLikePattern)
        }
    }

    @GetMapping("{id}")
    fun find(@PathVariable id: Long): Work {
        LOG.info("find work by id: ${id}")
        return workService.findById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody work: Work) {
        LOG.info("create work: ${work}")
        workService.save(work)
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Long, @RequestBody work: Work) {
        LOG.info("udpate work with id = ${id} work: ${work}")
        workService.update(id, work)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        LOG.info("delete work with id = ${id}")
        workService.delete(id)
    }
}
