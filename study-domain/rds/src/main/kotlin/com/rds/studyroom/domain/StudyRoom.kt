package com.rds.studyroom.domain

import com.rds.BaseTimeEntity
import com.rds.category.domain.Category
import jakarta.persistence.*

@Entity
class StudyRoom (
    val title: String,
    val explanation: String,
    @OneToOne(fetch = FetchType.LAZY)
    val category: Category,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    @Enumerated(EnumType.STRING)
    val state: StudyRoomState = StudyRoomState.ACTIVATED
}