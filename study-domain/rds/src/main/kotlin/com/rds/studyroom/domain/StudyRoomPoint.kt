package com.rds.studyroom.domain

import com.rds.BaseTimeEntity
import jakarta.persistence.*

@Entity
class StudyRoomPoint (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyRoomId")
    val studyRoom: StudyRoom,
    var point: Int
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun add(point: Int) {
        this.point += point
    }
}