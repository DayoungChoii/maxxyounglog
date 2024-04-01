package com.async.user

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class UserPointProducer(
    private val kafkaTemplate: KafkaTemplate<Long, Int>
): UserPointService {
    override fun createPoint(userId: Long, point: Int) {
        kafkaTemplate.send("create-user-point", userId, point)
    }

    override fun addPoint(userId: Long, point: Int) {
        kafkaTemplate.send("add-user-point", userId, point)
    }

    override fun subPoint(userId: Long, point: Int) {
        kafkaTemplate.send("sub-user-point", userId, point)
    }
}