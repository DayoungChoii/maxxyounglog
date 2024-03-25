package com.rds.studyroom.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class StudyRoom (
    val title: String,
    val explanation: String,
    val categoryId: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    val state: StudyRoomState = StudyRoomState.ACTIVATED
}