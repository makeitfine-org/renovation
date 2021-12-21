/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.web.interceptor

import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import renovation.data.exception.WorkNotFoundException

@Slf4j
@ControllerAdvice
class GlobalControllerExceptionHandler {
    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(this.javaClass)

        val WORK_NOT_FOUND = "work not found"
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WorkNotFoundException::class)
    fun handleTwinNotFoundException(e: WorkNotFoundException?) {
        LOG.error(WORK_NOT_FOUND, e)
    }
}
