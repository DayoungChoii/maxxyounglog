package com.api.studycompletion.service

import com.api.studycompletion.constant.StudyCompletionCreationValidatorStatus
import com.api.studycompletion.constant.StudyCompletionCreationValidatorStatus.*
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomState
import com.rds.user.domain.User
import com.rds.user.domain.UserState

class StudyCompletionCreationValidator(
    val studyRoom: StudyRoom?,
    val user: User?
) {
    fun validate(): StudyCompletionCreationValidatorStatus {
        if(studyRoom == null || studyRoom.state == StudyRoomState.DISABLED){
            return INVALID_ROOM
        }

        if (user == null || user.state != UserState.ACTIVATED) {
            return INVALID_USER
        }

        return SUCCESS
    }
}