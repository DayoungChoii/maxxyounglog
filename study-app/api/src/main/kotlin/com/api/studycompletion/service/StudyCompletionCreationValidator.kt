package com.api.studycompletion.service

import com.api.common.Validator
import com.api.studycompletion.constant.StudyCompletionCreationValidatorStatus
import com.api.studycompletion.constant.StudyCompletionCreationValidatorStatus.*
import com.api.studycompletion.dto.StudyCompletionCreationRequest
import com.rds.studyroom.domain.StudyRoomState
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.user.domain.UserState
import com.rds.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class StudyCompletionCreationValidator(
    private val studyRoomRepository: StudyRoomRepository,
    private val userRepository: UserRepository
): Validator<StudyCompletionCreationRequest, StudyCompletionCreationValidatorStatus> {
    override fun validate(context: StudyCompletionCreationRequest): StudyCompletionCreationValidatorStatus {
        val studyRoom = studyRoomRepository.findByIdOrNull(context.studyRoomId)
        if(studyRoom == null || studyRoom.state == StudyRoomState.DISABLED){
            return INVALID_ROOM
        }

        val user = userRepository.findByIdOrNull(context.userId)
        if (user == null || user.state != UserState.ACTIVATED) {
            return INVALID_USER
        }

        return SUCCESS
    }
}