package com.api.studyroom.service

import com.api.common.const.CREATE_STUDY_POINT
import com.api.studyroom.constant.StudyRoomCreationStatus
import com.api.studyroom.constant.StudyRoomCreationStatus.SUCCESS
import com.api.studyroom.dto.SimpleStudyRoomDto
import com.api.studyroom.dto.StudyRoomCreationRequest
import com.api.studyroom.dto.StudyRoomListRequest
import com.api.studyroom.dto.StudyRoomListResponse
import com.api.studyroom.repository.StudyRoomQueryRepository
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.event.StudyRoomCreatedEvent
import com.rds.studyroom.repository.StudyRoomRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class StudyRoomService (
    private val studyRoomRepository: StudyRoomRepository,
    private val categoryRepository: CategoryRepository,
    private val studyRoomQueryRepository: StudyRoomQueryRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun createStudyRoom(request: StudyRoomCreationRequest): StudyRoomCreationStatus {
        val category = categoryRepository.findByIdOrNull(request.categoryId) ?: return StudyRoomCreationStatus.INVALID_CATEGORY
        val studyRoom = studyRoomRepository.save(request.toStudyRoom(category))

        eventPublisher.publishEvent(StudyRoomCreatedEvent(studyRoom.id, CREATE_STUDY_POINT))
        return SUCCESS
    }

    fun getStudyRoomList(request: StudyRoomListRequest): StudyRoomListResponse? {
        val findStudyRoomList = studyRoomQueryRepository.findStudyRoomList(request.studyRoomSearch, request.studyRoomId, request.pageSize)
        return StudyRoomListResponse(findStudyRoomList?.map{SimpleStudyRoomDto.of(it)} ?: null)
    }
}
