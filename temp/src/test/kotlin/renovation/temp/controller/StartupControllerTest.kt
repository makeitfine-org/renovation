/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.controller

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import renovation.temp.config.RsaKeyProperties
import renovation.temp.config.SecurityConfig
import renovation.temp.service.TokenService

@WebMvcTest(StartupController::class, TokenController::class)
@Import(SecurityConfig::class, TokenService::class, RsaKeyProperties::class)
class StartupControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun tokenWhenAnonymousThenStatusIsUnauthorized() {
        mvc.perform(MockMvcRequestBuilders.post("/token"))
            .andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
            )
    }

    @Test
    fun tokenWithBasicThenGetToken() {
        val result = mvc.perform(
            MockMvcRequestBuilders.post("/token")
                .with(
                    SecurityMockMvcRequestPostProcessors.httpBasic("test", "test")
                )
        ).andExpect(
            MockMvcResultMatchers.status().isOk()
        ).andReturn()

        Assertions.assertThat(result.response.contentAsString).isNotEmpty()
    }

    @Test
    fun tokenWithWrongCredentialsBasic() {
        val result = mvc.perform(
            MockMvcRequestBuilders.post("/token")
                .with(
                    SecurityMockMvcRequestPostProcessors.httpBasic("testWrong", "test")
                )
        ).andExpect(
            MockMvcResultMatchers.status().isUnauthorized
        ).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun rootWhenUnauthenticatedThen401() {
        mvc.perform(
            MockMvcRequestBuilders.get("/about")
        ).andExpect(
            MockMvcResultMatchers.status().isUnauthorized()
        )
    }

    @Test
    @Throws(Exception::class)
    fun rootWithBasicStatusIsUnauthorized() {
        mvc.perform(
            MockMvcRequestBuilders.get("/about")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("test", "testWrong"))
        ).andExpect(
            MockMvcResultMatchers.status().isUnauthorized()
        )
    }

    @Test
    @WithMockUser
    fun rootWithMockUserStatusIsOK() {
        mvc.perform(
            MockMvcRequestBuilders.get("/about")
        ).andExpect(
            MockMvcResultMatchers.status().isOk()
        )
    }
}
