package com.api.studyroom.service

import com.api.studyroom.constant.StudyRoomCreationStatus.INVALID_CATEGORY
import com.api.studyroom.constant.StudyRoomCreationStatus.SUCCESS
import com.api.studyroom.dto.StudyRoomCreationRequest
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.category.domain.Category
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.repository.StudyRoomRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull

class StudyRoomServiceTest: BehaviorSpec({
    val studyRoomRepository: StudyRoomRepository = mockk<StudyRoomRepository>()
    val categoryRepository: CategoryRepository = mockk<CategoryRepository>()
    val eventPublisher: ApplicationEventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
    val studyRoomService: StudyRoomService = StudyRoomService(studyRoomRepository, categoryRepository, eventPublisher)
    val fixture  = kotlinFixture()

    `given`("스터디방 생성 시") {
        val request = fixture<StudyRoomCreationRequest>()
        `when`("카테고리가 올바르지 않으면") {
            every { categoryRepository.findByIdOrNull(request.categoryId) } returns null
            `then`("유효하지 않은 카테고리 상태를 반환한다.") {
                studyRoomService.createStudyRoom(request) shouldBe INVALID_CATEGORY
            }
        }

        `when`("정상 처리 되면") {
            every { categoryRepository.findByIdOrNull(request.categoryId) } returns fixture<Category>()
            every { studyRoomRepository.save(any()) } returns fixture<StudyRoom>()
            every { eventPublisher.publishEvent(any()) } returns Unit
            `then`("완료 상태를 반환한다.") {
                studyRoomService.createStudyRoom(request) shouldBe SUCCESS
            }
        }
    }
})