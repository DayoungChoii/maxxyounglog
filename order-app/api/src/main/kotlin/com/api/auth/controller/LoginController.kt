package com.api.auth.controller

import com.api.auth.dto.LogInRequest
import com.api.auth.dto.LogInResponse
import com.api.auth.service.LoginService
import com.api.auth.status.LogInStatus
import com.api.auth.status.LogInStatus.*
import com.api.common.StatusResult
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
class LoginController(
    private val loginService: LoginService,
    @Value("\${domain}")
    val domain:String
) {

    @PostMapping("login")
    fun login(@Valid @RequestBody request: LogInRequest): ResponseEntity<StatusResult<LogInStatus>> {
        val logInResult = loginService.login(request)

        val loginResponse = ResponseEntity.status(HttpStatus.OK)
        if (logInResult.status == SUCCESS) {
            loginResponse.header(HttpHeaders.SET_COOKIE, createCookie(logInResult).toString())
        }
        return loginResponse.body(StatusResult(logInResult.status))

    }

    private fun createCookie(logInResponse: LogInResponse) =
        ResponseCookie.from("SESSION", logInResponse.sessionToken)
            .domain(domain)
            .path("/")
            .httpOnly(true)
            .secure(false)
            .maxAge(Duration.ofDays(30))
            .sameSite("Strict")
            .build()
}
