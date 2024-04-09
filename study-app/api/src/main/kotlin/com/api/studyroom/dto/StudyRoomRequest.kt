package com.api.studyroom.dto

import com.rds.category.domain.Category
import com.rds.studyroom.domain.StudyRoom
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class StudyRoomCreationRequest (
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val explanation: String,

    @field:NotNull
    val categoryId: Long,
) {
    fun toStudyRoom(category: Category) =
        StudyRoom(
            title = title,
            explanation = explanation,
            category = category
        )
}

data class StudyRoomListRequest(
    val studyRoomSearch: StudyRoomSearch,
    val studyRoomId: Long?,
    val pageSize: Long
)
