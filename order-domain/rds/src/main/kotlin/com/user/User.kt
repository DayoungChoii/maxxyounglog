package com.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class User(
    val email: String,
    val ci: UUID,
    val password: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    var state: UserState = UserState.ACTIVATED;
}