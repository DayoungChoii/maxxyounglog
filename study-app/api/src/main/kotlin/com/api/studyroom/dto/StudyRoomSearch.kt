package com.api.studyroom.dto

import com.rds.category.domain.Category

data class StudyRoomSearch (
    val title: String?,
    val category: Category?
)