package com.api.auth.filter

import com.api.auth.exception.AuthExceptionType
import com.api.common.StatusDataResult
import com.google.gson.Gson
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.OncePerRequestFilter

class JwtExceptionFilter: OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BadCredentialsException) {
            setErrorResponse(response, AuthExceptionType.INVALID_PASSWORD)
        } catch (e: UsernameNotFoundException) {
            setErrorResponse(response, AuthExceptionType.INVALID_EMAIL)
        } catch (e: ExpiredJwtException) {
            setErrorResponse(response, AuthExceptionType.EXPIRED_TOKEN)
        } catch (e: JwtException) {
            setErrorResponse(response, AuthExceptionType.INVALID_TOKEN)
        }
    }

    private fun setErrorResponse(response: HttpServletResponse, authExceptionType: AuthExceptionType) {
        response.status = authExceptionType.httpStatusCode.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(Gson().toJson(StatusDataResult(authExceptionType.name, authExceptionType.message)))
    }
}