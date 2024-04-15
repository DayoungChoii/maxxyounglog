package com.rds.studyroom.event

class StudyRoomCreatedEvent (
    val studyRoomId: Long,
    val studyPoint: Int
)

class StudyRoomJoinedEvent (
    val studyRoomId: Long,
    val userId: Long,
    val studyPoint: Int
)