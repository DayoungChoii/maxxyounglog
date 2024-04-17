package com.rds.studyroom.domain

import com.rds.BaseTimeEntity
import com.rds.user.domain.PointActionType
import com.rds.user.domain.User
import jakarta.persistence.*

@Entity
class StudyRoomPointLog (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id")
    val studyRoom: StudyRoom,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null,
    @Enumerated(EnumType.STRING)
    val pointActionType: PointActionType,
    val point: Int,
    @Enumerated(EnumType.STRING)
    val providerType: ProviderType
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}