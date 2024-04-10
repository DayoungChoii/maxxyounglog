package com.rds.studyroom.repository

import com.rds.studyroom.domain.UserStudyRoom
import org.springframework.data.jpa.repository.JpaRepository

interface UserStudyRoomRepository: JpaRepository<UserStudyRoom, Long> {
    fun findByStudyRoomIdAndUserId(studyRoomId: Long, userId: Long): UserStudyRoom?
}