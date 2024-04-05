package com.api.studyroom.event

import com.async.studyroom.StudyPointService
import com.rds.studyroom.event.StudyRoomCreatedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class StudyRoomEventListener (
    private val studyPointService: StudyPointService
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun createStudyRoomPoint(event: StudyRoomCreatedEvent) {
        studyPointService.createPoint(event.studyRoomId, event.studyPoint)
    }
}