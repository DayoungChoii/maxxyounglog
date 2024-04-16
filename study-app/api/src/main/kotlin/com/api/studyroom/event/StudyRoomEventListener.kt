package com.api.studyroom.event

import com.async.studyroom.StudyPointService
import com.rds.studyroom.event.StudyRoomCreatedEvent
import com.rds.studyroom.event.StudyRoomJoinedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class StudyRoomEventListener (
    private val studyPointService: StudyPointService
) {
    @EventListener
    fun createStudyRoomPoint(event: StudyRoomCreatedEvent) {
        studyPointService.createStudyRoom(event.studyRoomId, event.studyPoint)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun joinStudyPoint(event: StudyRoomJoinedEvent) {
        studyPointService.joinStudyRoom(event.studyRoomId, event.userId, event.studyPoint)
    }
}