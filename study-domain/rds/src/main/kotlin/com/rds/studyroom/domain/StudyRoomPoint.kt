package com.rds.studyroom.domain

import com.rds.BaseTimeEntity
import jakarta.persistence.*

@Entity
class StudyRoomPoint (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyRoomId")
    val studyRoom: StudyRoom
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    var point: Int = 0


}