/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.web.interceptor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import renovation.data.exception.WorkNotFoundException

@ControllerAdvice
class GlobalControllerExceptionHandler {
    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler::class.java)

        const val WORK_NOT_FOUND = "work not found"
        const val INTERNAL_SERVER_ERROR = "Internal server error (500)"
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WorkNotFoundException::class)
    fun handleTwinNotFoundException(e: WorkNotFoundException?) {
        LOG.error(WORK_NOT_FOUND, e)
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalServerError(e: Exception?): ResponseEntity<*> {
        LOG.error("internal server error", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(INTERNAL_SERVER_ERROR)
    }
}
