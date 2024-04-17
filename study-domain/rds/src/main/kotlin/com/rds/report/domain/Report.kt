package com.rds.report.domain

import com.rds.BaseTimeEntity
import com.rds.user.domain.User
import jakarta.persistence.*

@Entity
class Report (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}