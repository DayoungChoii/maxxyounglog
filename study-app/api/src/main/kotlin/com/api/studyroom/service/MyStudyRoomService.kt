package com.api.studyroom.service

import com.api.studyroom.dto.MyStudyRoomDetailResponse
import com.api.studyroom.dto.SimpleStudyRoomDto
import com.api.studyroom.dto.StudyRoomListResponse
import com.api.studyroom.repository.MyStudyRoomQueryRepository
import com.rds.studyroom.repository.StudyRoomPointRepository
import com.rds.studyroom.repository.UserStudyRoomRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MyStudyRoomService (
    private val userStudyRoomRepository: UserStudyRoomRepository,
    private val studyRoomRepository: MyStudyRoomQueryRepository,
    private val studyRoomPointRepository: StudyRoomPointRepository
) {

    @Cacheable(cacheNames = ["shortTerm"])
    fun getStudyRoomList(userId: Long): StudyRoomListResponse? {
        val findUserStudyRooms = userStudyRoomRepository.findByUserId(userId)
        return StudyRoomListResponse(findUserStudyRooms?.map { SimpleStudyRoomDto.of(it.studyRoom) } ?: null)
    }

    @Cacheable(cacheNames = ["longTerm"])
    fun getStudyRoomDetail(studyRoomId: Long): MyStudyRoomDetailResponse? {
        val findStudyRoom = studyRoomRepository.findMyStudyRoomDetail(studyRoomId)
        return findStudyRoom?.let {
            val studyRoomPoint = studyRoomPointRepository.findByStudyRoomId(it.id)
            MyStudyRoomDetailResponse.of(findStudyRoom, studyRoomPoint.point)
        } ?: null
    }

}
