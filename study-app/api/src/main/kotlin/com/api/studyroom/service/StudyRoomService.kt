package com.api.studyroom.service

import com.api.studyroom.constant.StudyRoomCreationStatus
import com.api.studyroom.constant.StudyRoomCreationStatus.SUCCESS
import com.api.studyroom.dto.StudyRoomCreationRequest
import com.api.studyroom.event.StudyRoomCreatedEvent
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.repository.StudyRoomRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudyRoomService (
    private val studyRoomRepository: StudyRoomRepository,
    private val categoryRepository: CategoryRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun createStudyRoom(request: StudyRoomCreationRequest): StudyRoomCreationStatus {
        val category = categoryRepository.findByIdOrNull(request.categoryId) ?: return StudyRoomCreationStatus.INVALID_CATEGORY
        val studyRoom = studyRoomRepository.save(request.toStudyRoom(category))

        eventPublisher.publishEvent(StudyRoomCreatedEvent(studyRoom.id))
        return SUCCESS
    }
}
