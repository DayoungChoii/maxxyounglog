package com.api.auth.service

import com.api.auth.JwtTokenHelper
import com.api.auth.constant.JwtTokenStatus
import com.api.auth.constant.JwtTokenStatus.*
import com.api.auth.constant.REFRESH_SECRET_KEY
import com.api.auth.dto.GenerateRefreshTokenRequest
import com.api.auth.exception.AuthException
import com.api.auth.exception.AuthExceptionType
import com.api.common.StatusDataResult
import io.jsonwebtoken.JwtException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class JwtTokenService (
    @Value("\${spring.jwt.token.access-expiration-time}")
    private val accessExpirationTime: Long,
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun generateRefreshToken(request: GenerateRefreshTokenRequest): StatusDataResult<JwtTokenStatus, String> {
        return try {
            if (isRefreshTokenValid(request.email, request.refreshToken)) {
                StatusDataResult(SUCCESS, JwtTokenHelper.createAccessToken(request.email, accessExpirationTime))
            } else {
                StatusDataResult(INVALID_REFRESH_TOKEN, "")
            }
        } catch (e: JwtException) {
            StatusDataResult(INVALID_REFRESH_TOKEN, "")
        } catch (e: AuthException) {
            StatusDataResult(EXPIRED_REFRESH_TOKEN, "")
        }
    }

    private fun isRefreshTokenValid(email:String, refreshToken: String): Boolean =
        isRefreshTokenParsable(refreshToken) && isRefreshTokenAlive(email)


    private fun isRefreshTokenParsable(refreshToken: String): Boolean =
        JwtTokenHelper.validateToken(refreshToken, REFRESH_SECRET_KEY)

    private fun isRefreshTokenAlive(email: String): Boolean {
        redisTemplate.opsForValue().get(email)
            ?: throw AuthException(AuthExceptionType.EXPIRED_TOKEN)
        return true
    }

}