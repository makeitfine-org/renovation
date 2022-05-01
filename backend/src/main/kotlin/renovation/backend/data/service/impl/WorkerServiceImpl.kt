/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import renovation.backend.data.domain.Worker
import renovation.backend.data.service.WorkerService

@Service
class WorkerServiceImpl(
    @Value("\${info-service.base-url}")
    private val infoServiceBaseUrl: String
) : WorkerService {
    override fun findAll(): List<Worker> {
        return listOf(
            Worker(name="Name 1", surname = "Surname 1", age = 27),
            Worker(name="Name 2", surname = "Surname 2", age = 38),
            Worker(name="Name 3", surname = "Surname 3", age = 41),
        )
    }
}
