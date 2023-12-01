package renovation.webflux.server.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import renovation.webflux.server.model.User
import renovation.webflux.server.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository
) {

    suspend fun saveUser(user: User): User? =
        userRepository.randomNameFindByEmail(user.email)
            .firstOrNull()
            ?.let { throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified email is already in use.") }
            ?: userRepository.save(user)

    suspend fun findAllUsers(): Flow<User> =
        userRepository.findAll()

    suspend fun findUserById(id: Long): User? =
        userRepository.findById(id)

    suspend fun deleteUserById(id: Long) {
        val foundUser = userRepository.findById(id)

        if (foundUser == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $id was not found.")
        } else {
            userRepository.deleteById(id)
        }
    }

    suspend fun updateUser(id: Long, requestedUser: User): User {
        val foundUser = userRepository.findById(id)

        return if (foundUser == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $id was not found.")
        } else {
            userRepository.save(
                requestedUser.copy(id = foundUser.id)
            )
        }
    }

    suspend fun findAllUsersByNameLike(name: String): Flow<User> = userRepository.findByNameContaining(name)

    suspend fun findUsersByCompanyId(id: Long): Flow<User> = userRepository.findByCompanyId(id)
}
