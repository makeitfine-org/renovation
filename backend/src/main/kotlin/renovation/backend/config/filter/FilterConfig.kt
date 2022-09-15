/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ServletComponentScan
class FilterConfig(
    val loggingFilter: LoggingFilter
) {

    @Bean
    fun apiWorkLoggingFilterReg() = FilterRegistrationBean<LoggingFilter>().also {
        it.filter = loggingFilter
        it.urlPatterns = listOf("/api/work")
    }
}
