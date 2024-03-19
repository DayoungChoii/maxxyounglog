package com.api.common.interceptor

import com.api.common.const.TOKEN_NAME
import com.api.common.exception.Unauthorized
import com.redis.auth.repository.SessionRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class AuthInterceptor(
    private val sessionRepository: SessionRepository
): HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val cookies = request.cookies
        cookies?.forEach {cookie ->
            if (cookie.name == TOKEN_NAME) {
                val sessionToken = cookie.value ?: throw Unauthorized()
                sessionRepository.findByToken(sessionToken) ?: throw Unauthorized()
            }
        } ?: throw Unauthorized()
        return true
    }
}