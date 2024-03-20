package com.api.auth

import com.api.auth.constant.ACCESS_SECRET_KEY
import com.api.auth.constant.REFRESH_SECRET_KEY
import com.api.auth.exception.AuthExceptionType
import com.api.auth.filter.JwtAuthorizationFilter
import io.jsonwebtoken.*
import java.util.*
import javax.crypto.SecretKey

class JwtTokenHelper {
    companion object {
        fun createAccessToken(email: String, accessExpirationTime: Long): String {
            val claims = Jwts.claims().apply { subject = email }
            val now = Date()
            val expiredDate = Date(now.time + accessExpirationTime)
            return Jwts.builder()
                .signWith(ACCESS_SECRET_KEY)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .compact()
        }

        fun createRefreshToken(email: String, refreshExpirationTime: Long): String {
            val claims = Jwts.claims().apply { subject = email }
            val now = Date()
            val expiredDate = Date(now.time + refreshExpirationTime)

            val refreshToken = Jwts.builder()
                .signWith(REFRESH_SECRET_KEY)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .compact()

            return refreshToken
        }

        fun validateToken(token: String, secretKey: SecretKey): Boolean {
            return try {
                parseJwtToken(token, secretKey)
                true
            } catch (e: ExpiredJwtException) {
                JwtAuthorizationFilter.log.info(AuthExceptionType.EXPIRED_TOKEN.message, e)
                throw e
            } catch (e: JwtException) {
                JwtAuthorizationFilter.log.info(AuthExceptionType.INVALID_TOKEN.message, e)
                throw e
            }
        }

        fun parseJwtToken(token: String, secretKey: SecretKey): Jws<Claims>? {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
        }
    }

}