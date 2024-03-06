package com.api.common

import org.springframework.http.ResponseEntity

fun <S> StatusResult<S>.toResponse() = ResponseEntity.ok(this)
fun <S, T> DataStatusResult<S, T>.toResponse() = ResponseEntity.ok(this)
fun <T> DataResult<T>.to400Response() = ResponseEntity.badRequest().body(this.data)
fun <T> DataResult<T>.to500Response() = ResponseEntity.internalServerError().body(this.data)

