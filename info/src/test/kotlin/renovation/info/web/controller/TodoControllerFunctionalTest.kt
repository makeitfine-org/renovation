package renovation.info.web.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Tag("functional")
@Testcontainers
class TodoControllerFunctionalTest {

    companion object {
        private const val MONGO_DB_DOCKER_IMAGE = "mongo:5.0.6"

        @Container
        val mongoDBContainer = MongoDBContainer(MONGO_DB_DOCKER_IMAGE)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("keycloak.enabled") { "false" }
            registry.add("spring.autoconfigure.exclude") {
                "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
                        "org.springframework.boot.actuate.autoconfigure.security.servlet" +
                        ".ManagementWebSecurityAutoConfiguration"
            }

//            registry.add("spring.data.mongodb.authentication-database") { mongoDBContainer. }
//            registry.add("spring.data.mongodb.database") { mongoDBContainer.getre }
//            registry.add("spring.data.mongodb.username") { mongoDBContainer.host }
//            registry.add("spring.data.mongodb.password") { mongoDBContainer.host }
//            registry.add("spring.data.mongodb.host") { mongoDBContainer.host }
//            registry.add("spring.data.mongodb.port") { mongoDBContainer.firstMappedPort }
////            registry.add("spring.datasource.password") { postgresContainer.password }
////            registry.add("spring.datasource.username") { postgresContainer.username }
//
//            registry.add("spring.redis.host") { redisContainer.host }
//            registry.add("spring.redis.port") { redisContainer.firstMappedPort }
//            registry.add("spring.redis.password") { "" }
        }
    }

    @Test
    fun findAll() {
    }

    @Test
    fun find() {
    }

    @Test
    fun create() {
    }

    @Test
    fun delete() {
    }
}
