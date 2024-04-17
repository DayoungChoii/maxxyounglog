package com.api.studyroom.dto

import com.rds.studyroom.domain.StudyRoom
import com.rds.user.domain.User

data class MyStudyRoomDetailResponse (
    val studyRoomId: Long,
    val title: String,
    val explanation: String,
    val category: String,
    val participantNum: Int,
    val participants: List<ParticipantDto>,
    val studyRoomPoint: Int
) {
    companion object {
        fun of(studyRoom: StudyRoom) =
            MyStudyRoomDetailResponse(
                studyRoomId = studyRoom.id,
                title = studyRoom.title,
                explanation = studyRoom.explanation,
                category = studyRoom.category.name,
                participantNum = studyRoom.userStudyRooms.size,
                participants = studyRoom.userStudyRooms.map { ParticipantDto.of( it.user ) },
                studyRoomPoint = studyRoom.studyRoomPoint?.point ?: 0
            )
    }
}

data class ParticipantDto (
    val name: String
) {
    companion object {
        fun of(user: User) = ParticipantDto(
            name = user.name
        )
    }
}
