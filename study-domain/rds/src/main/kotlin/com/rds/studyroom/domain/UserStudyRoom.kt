package com.rds.studyroom.domain

import com.rds.BaseTimeEntity
import com.rds.user.domain.User
import jakarta.persistence.*

@Entity
class UserStudyRoom (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id")
    val studyRoom: StudyRoom,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    @Enumerated(EnumType.STRING)
    var state: UserStudyRoomState = UserStudyRoomState.ACTIVATED

    fun joinStudyRoom() {
        this.state = UserStudyRoomState.ACTIVATED
    }
}