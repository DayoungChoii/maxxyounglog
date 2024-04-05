package com.api.auth.event

import com.async.user.UserPointService
import com.rds.user.event.UserSignedUpEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class AuthEventListener (
    private val userPointService: UserPointService
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun createStudyRoomPoint(event: UserSignedUpEvent) {
        userPointService.createPoint(event.userId, event.point)
    }
}