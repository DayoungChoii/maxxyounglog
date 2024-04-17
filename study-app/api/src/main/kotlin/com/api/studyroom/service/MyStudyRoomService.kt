package com.api.studyroom.service

import com.api.studyroom.dto.MyStudyRoomDetailResponse
import com.api.studyroom.dto.SimpleStudyRoomDto
import com.api.studyroom.dto.StudyRoomListResponse
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.studyroom.repository.UserStudyRoomRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MyStudyRoomService (
    private val userStudyRoomRepository: UserStudyRoomRepository,
    private val studyRoomRepository: StudyRoomRepository
) {

    @Cacheable(cacheNames = ["shortTerm"])
    fun getStudyRoomList(userId: Long): StudyRoomListResponse? {
        val findUserStudyRooms = userStudyRoomRepository.findByUserId(userId)
        return StudyRoomListResponse(findUserStudyRooms?.map { SimpleStudyRoomDto.of(it.studyRoom) } ?: null)
    }

    @Cacheable(cacheNames = ["longTerm"])
    fun getStudyRoomDetail(studyRoomId: Long): MyStudyRoomDetailResponse? {
        val findStudyRoom = studyRoomRepository.findByIdOrNull(studyRoomId)
        return findStudyRoom?.let {  MyStudyRoomDetailResponse.of(findStudyRoom) } ?: null
    }

}
