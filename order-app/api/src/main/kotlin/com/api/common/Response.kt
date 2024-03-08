package com.api.common

import org.springframework.http.ResponseEntity

fun <S> StatusResult<S>.toResponse() = ResponseEntity.ok(this)
fun <S> DataResult<S>.toResponse() = ResponseEntity.ok(this)


