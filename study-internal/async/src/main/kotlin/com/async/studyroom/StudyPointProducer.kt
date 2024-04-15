package com.async.studyroom

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class StudyPointProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
): StudyPointService {
    override fun createStudyRoom(studyRoomId: Long, point: Int) {
        kafkaTemplate.send("create-study-point", studyRoomId.toString(), point.toString())
    }

    override fun addPoint(userId: Long, point: Int) {
        kafkaTemplate.send("add-study-point", userId.toString(), point.toString())
    }

    override fun subPoint(userId: Long, point: Int) {
        kafkaTemplate.send("sub-study-point", userId.toString(), point.toString())
    }
}