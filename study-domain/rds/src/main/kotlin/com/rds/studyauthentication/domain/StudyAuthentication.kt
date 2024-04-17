package com.rds.studyauthentication.domain

import com.rds.BaseTimeEntity
import com.rds.studyroom.domain.StudyRoom
import com.rds.user.domain.User
import jakarta.persistence.*

@Entity
class StudyAuthentication (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id")
    val studyRoom: StudyRoom,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_file_name")
    val authFileId: AuthFile

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}