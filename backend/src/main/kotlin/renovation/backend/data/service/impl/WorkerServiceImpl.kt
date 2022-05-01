/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.service.impl

import org.springframework.stereotype.Service
import renovation.backend.data.service.GraphQlService
import renovation.backend.data.service.WorkerService

@Service
class WorkerServiceImpl(
    private val graphQlService: GraphQlService,
) : WorkerService {
    override fun findAll() = graphQlService.getDetails(
        """
            query{
              details{
                id
                name
                surname
                age
              }  
            }
        """.trimIndent()
    )
}
