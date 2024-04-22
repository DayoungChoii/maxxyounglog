package com.api.studycompletion.controller

import com.api.common.StatusResult
import com.api.common.toResponse
import com.api.studycompletion.constant.StudyCompletionCreationStatus
import com.api.studycompletion.dto.StudyCompletionCreationRequest
import com.api.studycompletion.service.StudyCompletionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class StudyCompletionController(
    private val studyCompletionService: StudyCompletionService,
) {

    @PostMapping("study/completion")
    fun createStudyRoom(@ModelAttribute request: StudyCompletionCreationRequest): ResponseEntity<StatusResult<StudyCompletionCreationStatus>> {
        return StatusResult(studyCompletionService.completeStudy(request)).toResponse()
    }

}