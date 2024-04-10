package com.api.common

interface Validator<T, R> {
    fun validate(context: T): R
}