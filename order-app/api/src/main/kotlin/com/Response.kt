package com

import org.springframework.http.ResponseEntity

fun <S> StatusResult<S>.toResponse() = ResponseEntity.ok(this)
fun <S, T> DataResponse<S, T>.toResponse() = ResponseEntity.ok(this)
