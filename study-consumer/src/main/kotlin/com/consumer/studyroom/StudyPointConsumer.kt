package com.consumer.studyroom

import com.rds.studyroom.repository.StudyRoomPointRepository
import org.springframework.stereotype.Component

@Component
class StudyPointConsumer (
    private val studyRoomPointRepository: StudyRoomPointRepository
) {

}