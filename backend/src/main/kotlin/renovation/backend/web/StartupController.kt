/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import java.io.IOException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger { }

@RestController
class StartupController(
    @Value("\${spring.application.name}")
    private val applicationName: String
) {

    companion object {
        const val ABOUT_GE_URL_CALL_AMOUNT = "ABOUT_GE_URL_CALL_AMOUNT"
    }

    @GetMapping("/project")
    fun index() = "Hi, it's \"${StringUtils.capitalize(applicationName)}\" project"

    @GetMapping("/about")
    fun about(httpSession: HttpSession): Any = object {
        val name = "renovation backend module"
        val description = "Main backend part of renovation project"
    }.also {
        var counter = httpSession.getAttribute(ABOUT_GE_URL_CALL_AMOUNT)
            ?.let { it as Int } ?: 0
        httpSession.setAttribute(ABOUT_GE_URL_CALL_AMOUNT, ++counter)

        log.debug { "amoutn of \"/get\": $counter " }
    }

    @GetMapping("/counter")
    fun counter(httpSession: HttpSession) = httpSession.getAttribute(ABOUT_GE_URL_CALL_AMOUNT).let {
        var counter = it?.let { it as Int } ?: 0
        httpSession.setAttribute(ABOUT_GE_URL_CALL_AMOUNT, ++counter)
        log.debug { "amount of \"/get\": $counter " }

        object {
            val counter = counter
        }
    }

//    @GetMapping(path = ["/logout"])
//    @Throws(ServletException::class, IOException::class)
//    fun logout(request: HttpServletRequest, response: HttpServletResponse) = request.let {
//        it.logout()
//        response.sendRedirect("/")
//    }

    @GetMapping(path = ["/logout-without-redirect"])
    @Throws(ServletException::class, IOException::class)
    fun logoutNoRedirect(request: HttpServletRequest) = request.logout()
}
