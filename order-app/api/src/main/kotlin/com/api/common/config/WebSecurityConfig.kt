package com.api.common.config

import com.api.auth.filter.JwtAuthenticationFilter
import com.api.auth.filter.JwtAuthorizationFilter
import com.api.auth.filter.JwtExceptionFilter
import com.api.auth.service.security.JwtTokenProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class WebSecurityConfig (
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: RedisTemplate<String, String>,
    @Value("\${spring.jwt.token.access-expiration-time}")
    private val accessExpirationTime: Long,
    @Value("\${spring.jwt.token.refresh-expiration-time}")
    private val refreshExpirationTime: Long
) {

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.authenticationProvider(jwtTokenProvider)
        return authenticationManagerBuilder.build()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        return http
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/signup", "/v1/login", "/accesstoken").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf{csrfConfig: CsrfConfigurer<HttpSecurity> -> csrfConfig.disable()}
            .sessionManagement{sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .addFilterBefore(JwtExceptionFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(JwtAuthenticationFilter(authenticationManager, redisTemplate, accessExpirationTime, refreshExpirationTime), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {access ->
                access.accessDeniedHandler{ _, response, _ ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                }
                access.authenticationEntryPoint{_, response, _ ->
                    response.status = HttpServletResponse.SC_FORBIDDEN
                }
            }.build()

    }

    @Bean
    fun ignoringCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().requestMatchers(
                "/resources/**",
                "/static/**",
                "/favicon.ico",
                "/error"
            )
        }
    }
}