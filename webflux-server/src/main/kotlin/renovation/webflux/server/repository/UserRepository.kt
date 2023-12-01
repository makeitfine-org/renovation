package renovation.webflux.server.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import renovation.webflux.server.model.User

interface UserRepository : CoroutineCrudRepository<User, Long> {

    fun findByNameContaining(name: String): Flow<User>

    fun findByCompanyId(companyId: Long): Flow<User>

    @Query("SELECT * FROM webflux.app_user WHERE email = :email")
    fun randomNameFindByEmail(email: String): Flow<User>
}
