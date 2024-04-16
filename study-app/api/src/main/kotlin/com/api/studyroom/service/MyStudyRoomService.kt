package com.api.studyroom.service

import com.api.studyroom.dto.SimpleStudyRoomDto
import com.api.studyroom.dto.StudyRoomListResponse
import com.rds.studyroom.repository.UserStudyRoomRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MyStudyRoomService (
    private val userStudyRoomRepository: UserStudyRoomRepository,
) {

    @Cacheable(cacheNames = ["shortTerm"])
    fun getStudyRoomList(userId: Long): StudyRoomListResponse? {
        val findUserStudyRooms = userStudyRoomRepository.findByUserId(userId)
        return StudyRoomListResponse(findUserStudyRooms?.map { SimpleStudyRoomDto.of(it.studyRoom) } ?: null)
    }

}
