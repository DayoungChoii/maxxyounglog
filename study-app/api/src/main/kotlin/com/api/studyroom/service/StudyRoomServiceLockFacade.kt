package com.api.studyroom.service

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class StudyRoomServiceLockFacade (
    private val studyRoomService: StudyRoomService,
    private val redissonClient: RedissonClient
) {

    fun joinStudyRoom(studyRoomId: Long, userId: Long) {
        val lock = redissonClient.getLock(studyRoomId.toString())

        try {
            val available = lock.tryLock(10, 1, TimeUnit.SECONDS)

            if (!available) {
                return
            }

            studyRoomService.joinStudyRoom(studyRoomId, userId)

        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }
    }
}