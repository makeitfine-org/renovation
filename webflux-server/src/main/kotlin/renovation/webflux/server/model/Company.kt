package renovation.webflux.server.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("r2.company")
data class Company(
    @Id val id: Long? = null,
    val name: String,
    val address: String
)
