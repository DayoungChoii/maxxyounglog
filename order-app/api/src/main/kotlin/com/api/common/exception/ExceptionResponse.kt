package com.api.common.exception

import org.springframework.http.ResponseEntity

fun ExceptionResult.toResponse() = ResponseEntity.status(this.code).body(this.message)