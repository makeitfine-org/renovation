package renovation.webflux.server.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import renovation.webflux.server.model.Company

interface CompanyRepository : CoroutineCrudRepository<Company, Long> {
    fun findByNameContaining(name: String): Flow<Company>
}
