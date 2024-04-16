package com.api.studyroom.controller

import com.api.auth.service.SecurityContextHolderFacade
import com.api.common.DataResult
import com.api.common.toResponse
import com.api.studyroom.dto.StudyRoomListResponse
import com.api.studyroom.service.MyStudyRoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyStudyRoomController (
    private val myStudyRoomService: MyStudyRoomService
) {
    @GetMapping("my/studyrooms")
    fun myStudyRooms(): ResponseEntity<DataResult<StudyRoomListResponse?>> {
        val userId = SecurityContextHolderFacade.getId()
        return DataResult(myStudyRoomService.getStudyRoomList(userId)).toResponse()
    }
}