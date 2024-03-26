package com.api.studyroom.event

import com.rds.studyroom.domain.StudyRoomPoint
import com.rds.studyroom.repository.StudyRoomPointRepository
import com.rds.studyroom.repository.StudyRoomRepository
import org.springframework.context.event.EventListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class StudyRoomEventListener (
    private val studyRoomPointRepository: StudyRoomPointRepository,
    private val studyRoomRepository: StudyRoomRepository
) {
    @EventListener
    fun createStudyRoomPoint(event: StudyRoomCreatedEvent) {
        val studyRoom = studyRoomRepository.findByIdOrNull(event.studyRoomId)!!
        studyRoomPointRepository.save(StudyRoomPoint(studyRoom))
    }
}