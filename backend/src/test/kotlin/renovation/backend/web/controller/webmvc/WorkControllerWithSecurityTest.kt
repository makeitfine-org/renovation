/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web.controller.webmvc

import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@TestPropertySource(properties = ["keycloak.enabled=false"])
@WithMockUser(roles = ["WORK"])
internal class WorkControllerWithSecurityTest : WorkControllerTestAbstract() {

    override fun patch(urlTemplate: String, vararg uriVars: Any?) =
        MockMvcRequestBuilders.patch(urlTemplate, *uriVars).with(csrf())

    override fun post(urlTemplate: String, vararg uriVars: Any?) =
        MockMvcRequestBuilders.post(urlTemplate, *uriVars).with(csrf())

    override fun delete(urlTemplate: String, vararg uriVars: Any?) =
        MockMvcRequestBuilders.delete(urlTemplate, *uriVars).with(csrf())
}
