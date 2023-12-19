package renovation.common.security.jwt

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

private typealias JsonObj = Map<*, *>
private typealias JSonArr = List<*>

object JwtUtils {

    @Suppress("ClassNaming")
    enum class RoleCase {
        LOWERCASE,
        UPPERCASE
    }

    @JvmStatic
    @Suppress("NestedBlockDepth")
    fun jwtGrantedAuthoritiesConverter(
        clientId: String,
        rolesUppercase: RoleCase = RoleCase.LOWERCASE
    ): Converter<Jwt, Collection<GrantedAuthority>> {
        val delegate = JwtGrantedAuthoritiesConverter()

        return object : Converter<Jwt, Collection<GrantedAuthority>> {

            @Suppress("ReturnCount")
            override fun convert(jwt: Jwt): Collection<GrantedAuthority>? {
                val grantedAuthorities = delegate.convert(jwt)
                if (jwt.getClaim<Any>("realm_access") == null) {
                    return grantedAuthorities
                }
                val realmAccess = jwt.getClaim<JsonObj>("realm_access")
                if (realmAccess["roles"] == null) {
                    return grantedAuthorities
                }
                val roles = realmAccess["roles"] as JSonArr
                val keycloakAuthorities = roles.filterNotNull().map { role: Any ->
                    SimpleGrantedAuthority("ROLE_$role")
                }.toList()
                grantedAuthorities!!.addAll(keycloakAuthorities)

                grantedAuthorities.addAll(
                    clientRolesAuthorities(clientId, jwt, rolesUppercase)
                )

                return grantedAuthorities
            }
        }
    }

    @JvmStatic
    @Suppress("NestedBlockDepth")
    fun clientRolesAuthorities(
        clientId: String,
        jwt: Jwt,
        rolesUppercase: RoleCase = RoleCase.LOWERCASE
    ): Collection<GrantedAuthority> {
        val grantedAuthorities: MutableCollection<GrantedAuthority> = mutableListOf()

        jwt.getClaim<Any>("resource_access")?.let {
            (it as? JsonObj)?.let {
                (it[clientId] as? JsonObj)?.let {
                    (it["roles"] as? JSonArr)?.let {
                        it.filterNotNull().forEach {
                            grantedAuthorities.add(
                                SimpleGrantedAuthority(
                                    "ROLE_${
                                        if (rolesUppercase == RoleCase.UPPERCASE) {
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
