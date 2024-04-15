package com.async.studyroom

interface StudyPointService {
    fun createStudyRoom(userId: Long, point: Int)
    fun addPoint(userId: Long, point: Int)
    fun subPoint(userId: Long, point: Int)
}