package com.async.studyroom

interface StudyPointService {
    fun createStudyRoom(studyRoomId: Long, point: Int)
    fun joinStudyRoom(studyRoomId: Long, userId: Long, point: Int)
}