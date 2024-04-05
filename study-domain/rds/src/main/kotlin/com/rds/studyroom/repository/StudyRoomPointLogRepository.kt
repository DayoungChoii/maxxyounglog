package com.rds.studyroom.repository

import com.rds.studyroom.domain.StudyRoomPointLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudyRoomPointLogRepository : JpaRepository<StudyRoomPointLog, Long>