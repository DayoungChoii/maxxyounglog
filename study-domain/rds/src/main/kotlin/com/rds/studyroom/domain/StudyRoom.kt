package com.rds.studyroom.domain

import com.rds.BaseTimeEntity
import com.rds.category.domain.Category
import jakarta.persistence.*

@Entity
class StudyRoom (
    val title: String,
    val explanation: String,
    @ManyToOne(fetch = FetchType.LAZY)
    val category: Category,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    @Enumerated(EnumType.STRING)
    var state: StudyRoomState = StudyRoomState.ACTIVATED
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studyRoom")
    val userStudyRooms: List<UserStudyRoom> = ArrayList()
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "studyRoom")
    val studyRoomPoint: StudyRoomPoint? = null

}