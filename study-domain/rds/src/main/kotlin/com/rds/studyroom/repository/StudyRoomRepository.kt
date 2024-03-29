package com.rds.studyroom.repository

import com.rds.studyroom.domain.StudyRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudyRoomRepository: JpaRepository<StudyRoom, Long>