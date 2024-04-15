package com.rds.user.domain

import com.rds.BaseTimeEntity
import jakarta.persistence.*

@Entity
class UserPoint (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: User,
    var point: Int
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun sub(point: Int) {
        this.point -= point
    }

}