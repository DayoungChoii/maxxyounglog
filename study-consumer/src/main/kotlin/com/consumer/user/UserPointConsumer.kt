package com.consumer.user

import com.rds.user.domain.PointActionType
import com.rds.user.domain.PointActionType.ADDED
import com.rds.user.domain.PointActionType.SUBTRACTED
import com.rds.user.domain.User
import com.rds.user.domain.UserPoint
import com.rds.user.domain.UserPointLog
import com.rds.user.repository.UserPointLogRepository
import com.rds.user.repository.UserPointRepository
import com.rds.user.repository.UserRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserPointConsumer(
    private val userPointRepository: UserPointRepository,
    private val userRepository: UserRepository,
    private val userPointLogRepository: UserPointLogRepository
) {
    @KafkaListener(topics = ["create-user-point"], groupId = "point")
    @Transactional
    fun createUserPoint(data: ConsumerRecord<String, String>) {
        val user = userRepository.findByIdOrNull(data.key().toLong())!!
        val point = data.value().toInt()
        saveUserPoint(user, point)
        saveUserPointLog(user, point, ADDED)
    }

    private fun saveUserPointLog(
        user: User,
        point: Int,
        type: PointActionType
    ) {
        val userPointLog = UserPointLog(
            user,
            point,
            type
        )
        userPointLogRepository.save(userPointLog)
    }

    private fun saveUserPoint(
        user: User,
        point: Int
    ) {
        val userPoint = UserPoint(
            user,
            point
        )
        userPointRepository.save(userPoint)
    }

    @KafkaListener(topics = ["sub-user-point"], groupId = "point")
    @Transactional
    fun subUserPoint(data: ConsumerRecord<String, String>) {
        val userId = data.key().toLong()
        val point = data.value().toInt()
        val user = userRepository.findByIdOrNull(userId)!!

        val findUserPoint = userPointRepository.findByUserId(userId)
        findUserPoint.sub(point)
        saveUserPointLog(user, point, SUBTRACTED)
    }

}