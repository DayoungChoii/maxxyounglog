package com.api.common.exception

import com.api.common.DataResult
import com.api.common.to400Response
import com.api.common.to500Response
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(e: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<String> {
        log.error("ERROR[] ${e.stackTraceToString()}");
        return DataResult("wrong parameter").to400Response()
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleException(e: RuntimeException, request: HttpServletRequest): ResponseEntity<String?> {
        log.error("ERROR[] ${e.message}"
                + "\n ${e.stackTraceToString()}");
        return DataResult(e.message).to500Response()
    }
}