package com.rds.user.domain

import com.rds.BaseTimeEntity
import jakarta.persistence.*

@Entity
class User(
    val email: String,
    var password: String,
    var name: String
) : BaseTimeEntity(){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    @Enumerated(EnumType.STRING)
    var state: UserState = UserState.ACTIVATED;

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateName(name: String) {
        this.name = name
    }


}