package com.consumer.studyroom

import com.rds.studyroom.domain.ProviderType
import com.rds.studyroom.domain.ProviderType.SYSTEM
import com.rds.studyroom.domain.ProviderType.USER
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomPoint
import com.rds.studyroom.domain.StudyRoomPointLog
import com.rds.studyroom.repository.StudyRoomPointLogRepository
import com.rds.studyroom.repository.StudyRoomPointRepository
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.user.domain.PointActionType
import com.rds.user.domain.PointActionType.ADDED
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

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
        saveStudyRoomPointLog(studyRoom, point, ADDED, SYSTEM)
    }

    @KafkaListener(topics = ["add-study-point"], groupId = "point")
    @Transactional
    fun addStudyPoint(data: ConsumerRecord<String, String>) {
        val studyRoomId = data.key().toLong()
        val point = data.value().toInt()
        val studyRoom = studyRoomRepository.findByIdOrNull(studyRoomId)!!

        val findStudyRoomPoint = studyRoomPointRepository.findByStudyRoomId(studyRoomId)
        findStudyRoomPoint.add(point)

        saveStudyRoomPointLog(studyRoom, point, ADDED, USER)
    }

    private fun saveStudyRoomPointLog(studyRoom: StudyRoom, point: Int, actionType: PointActionType, providerType: ProviderType) {
        studyRoomPointLogRepository.save(
            StudyRoomPointLog(
                studyRoom = studyRoom,
                pointActionType = actionType,
                point = point,
                providerType = providerType,
            )
        )
    }
}