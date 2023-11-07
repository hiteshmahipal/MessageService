package com.p2p.message.util

import com.p2p.message.dto.ResponseDto
import com.p2p.message.dto.failure
import com.p2p.message.exception.CustomHttpException
import com.p2p.message.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    companion object : Logging {
        private val log = logger()
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleAnyException(ex: Exception): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity(
            ResponseDto<Any>(
                status = failure,
                message = ex.message,
            ), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity(
            ResponseDto<Any>(
                status = failure,
                message = ex.message,
            ), HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(value = [CustomHttpException::class])
    fun handleCustomHttpException(ex: CustomHttpException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity(
            ResponseDto<Any>(
                status = failure,
                message = ex.message,
            ), ex.httpStatusCode
        )
    }
}
