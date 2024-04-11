package com.api.studyroom.service

import com.api.common.const.CREATE_STUDY_POINT
import com.api.studyroom.constant.StudyRoomCreationStatus
import com.api.studyroom.constant.StudyRoomCreationStatus.SUCCESS
import com.api.studyroom.constant.StudyRoomJoinStatus
import com.api.studyroom.constant.StudyRoomJoinValidatorStatus
import com.api.studyroom.dto.SimpleStudyRoomDto
import com.api.studyroom.dto.StudyRoomCreationRequest
import com.api.studyroom.dto.StudyRoomListRequest
import com.api.studyroom.dto.StudyRoomListResponse
import com.api.studyroom.repository.StudyRoomQueryRepository
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.domain.UserStudyRoom
import com.rds.studyroom.event.StudyRoomCreatedEvent
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.studyroom.repository.UserStudyRoomRepository
import com.rds.user.repository.UserRepository
import org.springframework.cache.annotation.Cacheable
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
    private val eventPublisher: ApplicationEventPublisher,
    private val studyRoomJoinValidator: StudyRoomJoinValidator,
    private val userStudyRoomRepository: UserStudyRoomRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createStudyRoom(request: StudyRoomCreationRequest): StudyRoomCreationStatus {
        val category = categoryRepository.findByIdOrNull(request.categoryId) ?: return StudyRoomCreationStatus.INVALID_CATEGORY
        val studyRoom = studyRoomRepository.save(request.toStudyRoom(category))

        eventPublisher.publishEvent(StudyRoomCreatedEvent(studyRoom.id, CREATE_STUDY_POINT))
        return SUCCESS
    }

    @Cacheable(cacheNames = ["shortTerm"])
    fun getStudyRoomList(request: StudyRoomListRequest): StudyRoomListResponse? {
        val findStudyRoomList = studyRoomQueryRepository.findStudyRoomList(request.studyRoomSearch, request.studyRoomId, request.pageSize)
        return StudyRoomListResponse(findStudyRoomList?.map{SimpleStudyRoomDto.of(it)} ?: null)
    }

    @Transactional
    fun joinStudyRoom(studyRoomId: Long, userId: Long): StudyRoomJoinStatus {
        val currentStudyRoomUserNum = userStudyRoomRepository.countByStudyRoom(studyRoomId)
        if(currentStudyRoomUserNum > 10) return StudyRoomJoinStatus.OUT_OF_STUDY_ROOM_USER

        val validateResult = studyRoomJoinValidator.validate(Pair(studyRoomId, userId))
        if(validateResult == StudyRoomJoinValidatorStatus.SUCCESS){
            saveUserStudyRoom(studyRoomId, userId)
        } else if (validateResult == StudyRoomJoinValidatorStatus.RESIGNED_USER) {
            updateUserStudyRoom(studyRoomId, userId)
        }
        return convertValidationToResponse(validateResult)
    }

    private fun updateUserStudyRoom(studyRoomId: Long, userId: Long) {
        val userStudyRoom = userStudyRoomRepository.findByStudyRoomIdAndUserId(studyRoomId, userId)!!
        userStudyRoom.joinStudyRoom()
    }

    private fun saveUserStudyRoom(studyRoomId: Long, userId: Long) {
        val studyRoom = studyRoomRepository.findByIdOrNull(studyRoomId)!!
        val user = userRepository.findByIdOrNull(userId)!!
        userStudyRoomRepository.save(UserStudyRoom(studyRoom, user))
    }

    fun convertValidationToResponse(validatorStatus: StudyRoomJoinValidatorStatus): StudyRoomJoinStatus {
        return when(validatorStatus) {
            StudyRoomJoinValidatorStatus.INVALID_ROOM -> StudyRoomJoinStatus.INVALID_ROOM
            StudyRoomJoinValidatorStatus.BLOCKED_USER -> StudyRoomJoinStatus.BLOCKED_USER
            StudyRoomJoinValidatorStatus.KICKED_OUT_USER -> StudyRoomJoinStatus.KICKED_OUT_USER
            StudyRoomJoinValidatorStatus.SUCCESS,
            StudyRoomJoinValidatorStatus.RESIGNED_USER -> StudyRoomJoinStatus.SUCCESS
        }
    }

}
