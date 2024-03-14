package com.api.common.interceptor

import com.api.common.const.SESSION_NAME
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class AuthInterceptor: HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val cookies = request.cookies
        cookies?.forEach {
            it.name.equals(SESSION_NAME)
        }

        return true
    }
}