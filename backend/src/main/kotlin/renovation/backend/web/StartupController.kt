/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class StartupController(
    @Value("\${spring.application.name}")
    private val applicationName: String
) {
    @GetMapping("/project")
    fun index() = "Hi, it's \"${StringUtils.capitalize(applicationName)}\" project"

    @GetMapping("/about")
    fun about(): Any = object : Any() {
        val name = "renovation backend module"
        val description = "Main backend part of renovation project"
    }

    @GetMapping(path = ["/logout"])
    @Throws(ServletException::class, IOException::class)
    fun logout(request: HttpServletRequest, response: HttpServletResponse) = request.let {
        it.logout()
        response.sendRedirect("/")
    }

    @GetMapping(path = ["/logout-without-redirect"])
    @Throws(ServletException::class, IOException::class)
    fun logoutNoRedirect(request: HttpServletRequest) = request.logout()
}
