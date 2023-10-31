package renovation.common.security.jwt

import com.nimbusds.jose.shaded.gson.JsonArray
import com.nimbusds.jose.shaded.gson.JsonObject
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

object JwtUtils {

    @Suppress("ClassNaming")
    enum class ROLE_CASE {/**/
        LOWERCASE,
        UPPERCASE
    }

    @JvmStatic
    @Suppress("NestedBlockDepth")
    fun jwtGrantedAuthoritiesConverter(
        clientId: String,
        rolesUppercase: ROLE_CASE = ROLE_CASE.LOWERCASE
    ): Converter<Jwt, Collection<GrantedAuthority>> {
        val delegate = JwtGrantedAuthoritiesConverter()

        return object : Converter<Jwt, Collection<GrantedAuthority>> {

            @Suppress("ReturnCount")
            override fun convert(jwt: Jwt): Collection<GrantedAuthority>? {
                val grantedAuthorities = delegate.convert(jwt)
                if (jwt.getClaim<Any>("realm_access") == null) {
                    return grantedAuthorities
                }
                val realmAccess = jwt.getClaim<JsonObject>("realm_access")
                if (realmAccess["roles"] == null) {
                    return grantedAuthorities
                }
                val roles = realmAccess["roles"] as JsonArray
                val keycloakAuthorities = roles.asIterable().map { role: Any ->
                    SimpleGrantedAuthority("ROLE_$role")
                }.toList()
                grantedAuthorities!!.addAll(keycloakAuthorities)

                addClientRoles(jwt, grantedAuthorities)

                return grantedAuthorities
            }

            private fun addClientRoles(jwt: Jwt, grantedAuthorities: MutableCollection<GrantedAuthority>) {
                jwt.getClaim<Any>("resource_access")?.let {
                    (it as? JsonObject)?.let {
                        (it[clientId] as? JsonObject)?.let {
                            (it["roles"] as? JsonArray)?.let {
                                val clientRoles = it.asIterable().map { r: Any ->
                                    SimpleGrantedAuthority(
                                        "ROLE_${
                                            if (rolesUppercase == ROLE_CASE.UPPERCASE) {
                                                r.toString().uppercase()
                                            } else {
                                                r.toString().lowercase()
                                            }
                                        }"
                                    )
                                }.toList()

                                grantedAuthorities.addAll(clientRoles)
                            }
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    @Suppress("NestedBlockDepth")
    fun clientRolesAuthorities(
        clientId: String,
        jwt: Jwt,
        rolesUppercase: ROLE_CASE = ROLE_CASE.LOWERCASE
    ): Collection<GrantedAuthority> {
        val grantedAuthorities: MutableCollection<GrantedAuthority> = mutableListOf()

        jwt.getClaim<Any>("resource_access")?.let {
            (it as? JsonObject)?.let {
                (it[clientId] as? JsonObject)?.let {
                    (it["roles"] as? JsonArray)?.let {
                        it.asIterable().forEach {
                            grantedAuthorities.add(
                                SimpleGrantedAuthority(
                                    "ROLE_${
                                        if (rolesUppercase == ROLE_CASE.UPPERCASE) {
                                            it.toString().uppercase()
                                        } else {
                                            it.toString().lowercase()
                                        }
                                    }"
                                )
                            )
                        }
                    }
                }
            }
        }

        return grantedAuthorities
    }
}
