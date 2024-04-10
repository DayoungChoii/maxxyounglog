package com.api.studyroom.controller

import com.api.common.DataResult
import com.api.common.StatusResult
import com.api.common.toResponse
import com.api.studyroom.constant.StudyRoomCreationStatus
import com.api.studyroom.constant.StudyRoomJoinStatus
import com.api.studyroom.dto.StudyRoomCreationRequest
import com.api.studyroom.dto.StudyRoomListRequest
import com.api.studyroom.dto.StudyRoomListResponse
import com.api.studyroom.service.StudyRoomService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class StudyRoomController (
    private val studyRoomService: StudyRoomService
) {

    @PostMapping("studyroom")
    fun createStudyRoom(@Valid @RequestBody request: StudyRoomCreationRequest): ResponseEntity<StatusResult<StudyRoomCreationStatus>> {
        return StatusResult(studyRoomService.createStudyRoom(request)).toResponse()
    }

    @GetMapping("studyrooms")
    fun getStudyRoomList(@RequestBody request: StudyRoomListRequest): ResponseEntity<DataResult<StudyRoomListResponse?>> {
        return DataResult(studyRoomService.getStudyRoomList(request)).toResponse()
    }

    @PostMapping("studyroom/{studyRoomId}/user/{userId}")
    fun joinStudyRoom(@PathVariable studyRoomId: Long, @PathVariable userId: Long): ResponseEntity<StatusResult<StudyRoomJoinStatus>> {
        return StatusResult(studyRoomService.joinStudyRoom(studyRoomId, userId)).toResponse()
    }

}