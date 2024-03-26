package com.rds.studyroom.repository

import com.rds.studyroom.domain.StudyRoomPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudyRoomPointRepository : JpaRepository<StudyRoomPoint, Long>