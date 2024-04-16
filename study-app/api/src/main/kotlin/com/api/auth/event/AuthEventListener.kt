package com.api.auth.event

import com.async.user.UserPointService
import com.rds.user.event.UserSignedUpEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AuthEventListener (
    private val userPointService: UserPointService
) {
    @EventListener
    fun createStudyRoomPoint(event: UserSignedUpEvent) {
        userPointService.createPoint(event.userId, event.point)
    }
}