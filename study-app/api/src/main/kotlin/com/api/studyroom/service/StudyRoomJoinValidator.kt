package com.api.studyroom.service

import com.api.common.Validator
import com.api.studyroom.constant.StudyRoomJoinValidatorStatus
import com.api.studyroom.constant.StudyRoomJoinValidatorStatus.*
import com.rds.studyroom.domain.StudyRoomState
import com.rds.studyroom.domain.UserStudyRoomState
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.studyroom.repository.UserStudyRoomRepository
import com.rds.user.domain.UserState
import com.rds.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class StudyRoomJoinValidator (
    private val studyRoomRepository: StudyRoomRepository,
    private val userRepository: UserRepository,
    private val userStudyRoomRepository: UserStudyRoomRepository
): Validator<Pair<Long, Long>, StudyRoomJoinValidatorStatus> {
    override fun validate(context: Pair<Long, Long>): StudyRoomJoinValidatorStatus {
        val studyRoomId = context.first
        val userId = context.second

        val studyRoom = studyRoomRepository.findByIdOrNull(studyRoomId)
        if(studyRoom?.state == StudyRoomState.DISABLED){
            return INVALID_ROOM
        }

        val user = userRepository.findByIdOrNull(userId)
        if (user?.state == UserState.BLOCKED) {
            return BLOCKED_USER
        }

        val userStudyRoom = userStudyRoomRepository.findByStudyRoomIdAndUserId(studyRoomId, userId)
        if (userStudyRoom != null && userStudyRoom.state == UserStudyRoomState.BLOCKED) {
            return KICKED_OUT_USER
        } else if(userStudyRoom != null && userStudyRoom.state == UserStudyRoomState.DISABLED) {
            return RESIGNED_USER
        }

        return SUCCESS
    }
}