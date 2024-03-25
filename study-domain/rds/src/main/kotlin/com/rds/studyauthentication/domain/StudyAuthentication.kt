package com.rds.studyauthentication.domain

import com.rds.BaseTimeEntity
import com.rds.studyroom.domain.StudyRoom
import com.rds.user.domain.User
import jakarta.persistence.*

@Entity
class StudyAuthentication (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyRoomId")
    val studyRoom: StudyRoom,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: User,
    val fileName: String

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}