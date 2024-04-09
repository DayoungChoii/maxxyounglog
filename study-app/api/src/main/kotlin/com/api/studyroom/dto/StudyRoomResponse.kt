package com.api.studyroom.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomState

data class StudyRoomListResponse (
    @JsonProperty("simpleStudyRoomDtoList")
    val simpleStudyRoomDtoList: List<SimpleStudyRoomDto>?
)

data class SimpleStudyRoomDto (
    @JsonProperty("studyRoomId")
    val studyRoomId: Long,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("explanation")
    val explanation: String,
    @JsonProperty("category")
    val category: String,
    @JsonProperty("participantNum")
    val participantNum: Int,
    @JsonProperty("state")
    val state: StudyRoomState
) {
    companion object {
        fun of(studyRoom: StudyRoom) =
            SimpleStudyRoomDto(
                studyRoomId =  studyRoom.id,
                title = studyRoom.title,
                explanation = studyRoom.explanation,
                category = studyRoom.category.name,
                participantNum = studyRoom.userStudyRoom.size,
                state = studyRoom.state
            )
    }
}
