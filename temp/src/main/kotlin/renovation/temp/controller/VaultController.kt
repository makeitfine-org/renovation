package renovation.temp.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Profile("vault")
@RestController
@RequestMapping("/vault")
class VaultController(
    @Value("\${vaultValues.value1}")
    private val value1: String,
    @Value("\${vaultValues.value2}")
    private val value2: String,
) {
    @GetMapping
    fun list() = listOf(value1, value2)
}
