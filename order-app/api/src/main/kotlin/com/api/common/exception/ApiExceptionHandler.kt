package com.api.common.exception

import com.api.common.*
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


const val UNAUTHORIZED_MESSAGE: String = "invalid authorization"
const val PARAMETER_MESSAGE: String = "invalid parameter"
const val GLOBAL_MESSAGE: String = "internal server error"
@RestControllerAdvice
class ApiExceptionHandler {


    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @ExceptionHandler(Unauthorized::class)
    fun handleException(e: Unauthorized, request: HttpServletRequest): ResponseEntity<String> {
        log.error("ERROR[] ${e.stackTraceToString()}");
        return ExceptionResult(UNAUTHORIZED, UNAUTHORIZED_MESSAGE).toResponse()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(e: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<String> {
        log.error("ERROR[] ${e.stackTraceToString()}");
        return ExceptionResult(BAD_REQUEST, PARAMETER_MESSAGE).toResponse()
    }

    @ExceptionHandler(CustomException::class)
    fun handleException(e: CustomException, request: HttpServletRequest): ResponseEntity<String?> {
        log.error(
            "ERROR[] : ${e.type.message} \n ${e.stackTraceToString()}"
        );
        return ExceptionResult(e.type.httpStatusCode, e.type.message).toResponse()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): ResponseEntity<String?> {
        log.error("ERROR[] ${e.stackTraceToString()}");
        return ExceptionResult(INTERNAL_SERVER_ERROR, GLOBAL_MESSAGE).toResponse()
    }
}