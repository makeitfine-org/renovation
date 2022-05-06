/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.service

import renovation.backend.data.domain.Worker

interface WorkerService {
    fun findAll(): List<Worker>
}