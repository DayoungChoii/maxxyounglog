package com.rds.user.domain

import com.rds.BaseTimeEntity
import jakarta.persistence.*

@Entity
class UserPoint (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: User,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    val point: Int = 1000
}