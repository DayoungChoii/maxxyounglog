package com.rds.studyCompletion.domain

import com.rds.BaseTimeEntity
import com.rds.studyroom.domain.StudyRoom
import com.rds.user.domain.User
import jakarta.persistence.*

@Entity
class StudyCompletion (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id")
    val studyRoom: StudyRoom,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completion_file_name")
    val completionFile: CompletionFile

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}