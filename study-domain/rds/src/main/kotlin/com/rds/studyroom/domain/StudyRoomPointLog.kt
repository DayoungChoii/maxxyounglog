package com.rds.studyroom.domain

import com.rds.BaseTimeEntity
import com.rds.user.domain.User
import jakarta.persistence.*

@Entity
class StudyRoomPointLog (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyRoomId")
    val studyRoom: StudyRoom,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: User
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    var point: Int = 0
}