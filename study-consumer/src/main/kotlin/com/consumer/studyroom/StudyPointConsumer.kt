package com.consumer.studyroom

import com.rds.studyroom.domain.ProviderType
import com.rds.studyroom.domain.StudyRoomPoint
import com.rds.studyroom.domain.StudyRoomPointLog
import com.rds.studyroom.repository.StudyRoomPointLogRepository
import com.rds.studyroom.repository.StudyRoomPointRepository
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.user.domain.PointActionType
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class StudyPointConsumer (
    private val studyRoomPointRepository: StudyRoomPointRepository,
    private val studyRoomRepository: StudyRoomRepository,
    private val studyRoomPointLogRepository: StudyRoomPointLogRepository,
) {
    @KafkaListener(topics = ["create-study-point"], groupId = "point")
    fun createStudyPoint(data: ConsumerRecord<String, String>) {
        val studyRoomId = data.key().toLong()
        val point = data.value().toInt()
        val studyRoom = studyRoomRepository.findByIdOrNull(studyRoomId)!!
        studyRoomPointRepository.save(StudyRoomPoint(studyRoom, point))
        studyRoomPointLogRepository.save(
            StudyRoomPointLog(
                studyRoom = studyRoom,
                pointActionType = PointActionType.ADDED,
                point = point,
                providerType = ProviderType.SYSTEM,
            )
        )
    }
}