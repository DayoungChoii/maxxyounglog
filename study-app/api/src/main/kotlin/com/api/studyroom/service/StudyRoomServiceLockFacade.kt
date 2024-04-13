package com.api.studyroom.service

import com.api.common.RedisLockExecutor
import org.springframework.stereotype.Component

@Component
class StudyRoomServiceLockFacade (
    private val studyRoomService: StudyRoomService,
    private val redisLockExecutor: RedisLockExecutor
) {

    fun joinStudyRoom(studyRoomId: Long, userId: Long) {
        redisLockExecutor.execute(studyRoomId) {studyRoomService.joinStudyRoom(studyRoomId, userId)}
    }
}