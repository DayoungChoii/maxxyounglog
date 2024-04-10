package com.api.studyroom.service

import com.api.studyroom.StudyRoomProvider
import com.api.studyroom.constant.StudyRoomCreationStatus.INVALID_CATEGORY
import com.api.studyroom.constant.StudyRoomCreationStatus.SUCCESS
import com.api.studyroom.constant.StudyRoomJoinStatus
import com.api.studyroom.constant.StudyRoomJoinValidatorStatus
import com.api.studyroom.dto.StudyRoomCreationRequest
import com.api.studyroom.dto.StudyRoomListRequest
import com.api.studyroom.dto.StudyRoomSearch
import com.api.studyroom.repository.StudyRoomQueryRepository
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.category.domain.Category
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.UserStudyRoom
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.studyroom.repository.UserStudyRoomRepository
import com.rds.user.domain.User
import com.rds.user.repository.UserRepository
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
    val studyRoomQueryRepository: StudyRoomQueryRepository = mockk<StudyRoomQueryRepository>()
    val studyRoomJoinValidator: StudyRoomJoinValidator = mockk<StudyRoomJoinValidator>()
    val userStudyRoomRepository: UserStudyRoomRepository = mockk<UserStudyRoomRepository>()
    val userRepository: UserRepository = mockk<UserRepository>()
    val studyRoomService: StudyRoomService =
        StudyRoomService(
            studyRoomRepository,
            categoryRepository,
            studyRoomQueryRepository,
            eventPublisher,
            studyRoomJoinValidator,
            userStudyRoomRepository,
            userRepository
        )
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

    `given`("스터디방 목록 조회 시") {
        `when`("studyRoom 엔티티를 조회하면") {
            every {studyRoomQueryRepository.findStudyRoomList(any(StudyRoomSearch::class), any(Long::class), any(Long::class))} returns StudyRoomProvider.createStudyRooms(fixture<Category>(), "title", 10)
            `then` ("studyRoomListResponse로 반환한다.") {
                val studyRoomResponse = studyRoomService.getStudyRoomList(
                    StudyRoomListRequest(
                        studyRoomSearch = StudyRoomSearch("title", 3L),
                        studyRoomId = null,
                        pageSize = 10
                    )
                )
                studyRoomResponse!!.simpleStudyRoomDtoList!!.size shouldBe 10
                studyRoomResponse.simpleStudyRoomDtoList!!.first().title shouldBe "title1"
                studyRoomResponse.simpleStudyRoomDtoList!!.last().title shouldBe "title10"
            }
        }
    }

    `given`("스터디방 가입할 때") {
        val studyRoomId = 1L
        val userId = 1L
        `when`("유효성 검증에 성공하면") {
            every {studyRoomJoinValidator.validate(Pair(studyRoomId, userId))} returns StudyRoomJoinValidatorStatus.SUCCESS
            every {studyRoomRepository.findByIdOrNull(studyRoomId)} returns fixture<StudyRoom>()
            every {userRepository.findByIdOrNull(userId)} returns fixture<User>()
            every {userStudyRoomRepository.save(any(UserStudyRoom::class))} returns fixture<UserStudyRoom>()
            `then` ("스터디방 가입에 성공한다.") {
                studyRoomService.joinStudyRoom(studyRoomId, userId) shouldBe StudyRoomJoinStatus.SUCCESS
            }
        }
        `when`("스터디방을 스스로 나갔던 유저라면") {
            every {userStudyRoomRepository.findByStudyRoomIdAndUserId(studyRoomId, userId)} returns fixture<UserStudyRoom>()
        }
            `then` ("재가입에 성공한다.") {
                studyRoomService.joinStudyRoom(studyRoomId, userId) shouldBe StudyRoomJoinStatus.SUCCESS
        }
    }
})