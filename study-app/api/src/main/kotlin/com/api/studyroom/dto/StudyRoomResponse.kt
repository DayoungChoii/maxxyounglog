package com.api.studyroom.dto

import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomState

data class StudyRoomListResponse (

    val simpleStudyRoomDtoList: List<SimpleStudyRoomDto>?
)

data class SimpleStudyRoomDto (
    val studyRoomId: Long,
    val title: String,
    val explanation: String,
    val category: String,
    val participantNum: Int,
    val state: StudyRoomState
) {
    companion object {
        fun of(studyRoom: StudyRoom) =
            SimpleStudyRoomDto(
                studyRoomId =  studyRoom.id,
                title = studyRoom.title,
                explanation = studyRoom.explanation,
                category = studyRoom.category.name,
                participantNum = studyRoom.userStudyRooms.size,
                state = studyRoom.state
            )
    }
}
