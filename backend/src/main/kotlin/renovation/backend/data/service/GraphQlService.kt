/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.data.service

import renovation.backend.data.domain.Worker

interface GraphQlService {
    fun getDetails(graphQlBody: String): List<Worker>
}
