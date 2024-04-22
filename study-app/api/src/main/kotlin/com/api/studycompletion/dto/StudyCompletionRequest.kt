package com.api.studycompletion.dto

import org.springframework.web.multipart.MultipartFile

data class StudyCompletionCreationRequest (
    val studyRoomId: Long,
    val userId: Long,
    val file: MultipartFile
)