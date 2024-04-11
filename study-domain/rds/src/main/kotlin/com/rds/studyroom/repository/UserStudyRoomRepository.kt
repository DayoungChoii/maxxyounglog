package com.rds.studyroom.repository

import com.rds.studyroom.domain.UserStudyRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserStudyRoomRepository: JpaRepository<UserStudyRoom, Long> {
    fun findByStudyRoomIdAndUserId(studyRoomId: Long, userId: Long): UserStudyRoom?
    @Query("select count(u) from UserStudyRoom u where u.studyRoom.id = ?1")
    fun countByStudyRoom(studyRoomId: Long): Int
}