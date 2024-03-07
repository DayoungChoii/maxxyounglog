package com.api.auth.controller

import com.api.auth.dto.SignUpRequest
import com.api.auth.exception.AuthException
import com.api.auth.service.SignUpService
import com.api.auth.status.AuthExceptionMessage
import com.api.common.StatusResult
import com.api.common.toResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SignUpController (
    private val signUpService: SignUpService
){

    @PostMapping("signup")
    fun singUp(@Valid @RequestBody signUpRequest: SignUpRequest) =
        StatusResult(signUpService.signUp(signUpRequest)).toResponse()

    @GetMapping("/test")
    fun test() {
        throw AuthException(AuthExceptionMessage.SIGN_UP_FAIL, IllegalArgumentException())
    }

}