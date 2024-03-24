package com.api.auth.controller

import com.api.auth.constant.JwtTokenStatus
import com.api.auth.dto.GenerateRefreshTokenRequest
import com.api.auth.service.JwtTokenService
import com.api.common.StatusDataResult
import com.api.common.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class JwtTokenController (
    private val jwtTokenService: JwtTokenService
) {

    @PostMapping("accesstoken")
    fun generateAccessToken(@RequestBody request: GenerateRefreshTokenRequest): ResponseEntity<StatusDataResult<JwtTokenStatus, String>> {
        return jwtTokenService.generateRefreshToken(request).toResponse()
    }
}