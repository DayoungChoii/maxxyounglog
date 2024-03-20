package com.api.auth.constant

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

val ACCESS_SECRET_KEY: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
val REFRESH_SECRET_KEY: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)