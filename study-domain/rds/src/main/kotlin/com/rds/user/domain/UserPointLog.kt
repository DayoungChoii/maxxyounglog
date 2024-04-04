package com.rds.user.domain

import com.rds.BaseTimeEntity
import jakarta.persistence.*

@Entity
class UserPointLog (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: User,
    val point: Int,
    @Enumerated(EnumType.STRING)
    val actionType: PointActionType
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}