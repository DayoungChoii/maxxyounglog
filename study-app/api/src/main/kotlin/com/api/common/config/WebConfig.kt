package com.api.common.config

import com.api.common.interceptor.AuthInterceptor
import com.redis.auth.repository.SessionRepository
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
class WebConfig(
    private val sessionRepository: SessionRepository
): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(AuthInterceptor(sessionRepository))
            .order(0)
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/", "/signup", "/login", "/logout"
            )

    }
}