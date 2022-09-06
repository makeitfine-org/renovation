/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.config.security

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["keycloak.enabled"], havingValue = "true", matchIfMissing = true)
class KeycloakConfig {

    @Bean
    fun keycloakConfigResolver() = KeycloakSpringBootConfigResolver()
}
