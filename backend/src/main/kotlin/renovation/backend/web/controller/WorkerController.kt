/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.backend.data.domain.Worker
import renovation.backend.data.service.WorkerService

@CrossOrigin(originPatterns = ["http://localhost:80*", "http://r"])
@RestController
@RequestMapping("/api/worker")
class WorkerController(private val workerService: WorkerService) {

    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(WorkerController::class.java)
    }

    @GetMapping
    fun list(): List<Worker> {
        LOG.info("find all workers")
        return workerService.findAll()
    }
}
