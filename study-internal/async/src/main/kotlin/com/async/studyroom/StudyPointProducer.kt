package com.async.studyroom

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StudyPointProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
): StudyPointService {
    override fun createStudyRoom(studyRoomId: Long, point: Int) {
        kafkaTemplate.send("create-study-point", studyRoomId.toString(), point.toString())
    }

    @Transactional
    override fun joinStudyRoom(studyRoomId:Long, userId: Long, point: Int) {
        kafkaTemplate.send("add-study-point", studyRoomId.toString(), point.toString())
        kafkaTemplate.send("sub-user-point", userId.toString(), point.toString())
    }
}