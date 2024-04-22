package com.api.studycompletion.event

import com.async.user.UserPointService
import com.rds.studyCompletion.event.StudyCompletionCreatedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class StudyCompletionEventListener (
    private val userPointService: UserPointService
) {
    @EventListener
    fun createStudyRoomPoint(event: StudyCompletionCreatedEvent) {
        userPointService.addPoint(event.userId, event.userPoint)
    }
}