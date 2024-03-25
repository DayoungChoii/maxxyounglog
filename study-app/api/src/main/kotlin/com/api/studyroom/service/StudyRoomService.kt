package com.api.studyroom.service

import com.api.studyroom.constant.StudyRoomCreationStatus
import com.api.studyroom.constant.StudyRoomCreationStatus.INVALID_CATEGORY
import com.api.studyroom.constant.StudyRoomCreationStatus.SUCCESS
import com.api.studyroom.dto.StudyRoomCreationRequest
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.repository.StudyRoomRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StudyRoomService (
    private val studyRoomRepository: StudyRoomRepository,
    private val categoryRepository: CategoryRepository
) {
    fun createStudyRoom(request: StudyRoomCreationRequest): StudyRoomCreationStatus {
        val category = categoryRepository.findByIdOrNull(request.categoryId) ?: return INVALID_CATEGORY
        studyRoomRepository.save(request.toStudyRoom(category))
        return SUCCESS
    }
}
