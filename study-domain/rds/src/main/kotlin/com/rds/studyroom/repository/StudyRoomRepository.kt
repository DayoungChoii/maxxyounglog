package com.rds.studyroom.repository

import com.rds.studyroom.domain.StudyRoom
import org.springframework.data.repository.CrudRepository

interface StudyRoomRepository: CrudRepository<StudyRoom, Long>