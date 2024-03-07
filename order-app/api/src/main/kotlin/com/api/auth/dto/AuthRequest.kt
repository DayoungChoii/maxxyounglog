package com.api.auth.dto

import com.rds.user.domain.User
import jakarta.validation.constraints.NotBlank

data class SignUpRequest (
    @field:NotBlank
    val email: String,

    @field:NotBlank
    var password: String,

    @field:NotBlank
    val name: String,

) {

    fun toUser() =
        User(
            email = this.email,
            password = this.password,
            name = this.name
        )

    fun encryptPassword(password: String) {
        this.password = password
    }

}
