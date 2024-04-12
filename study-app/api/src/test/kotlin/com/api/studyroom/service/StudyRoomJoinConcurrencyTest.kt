package com.api.studyroom.service

import com.api.common.IntegrationTest
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.category.domain.Category
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomState
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.studyroom.repository.UserStudyRoomRepository
import com.rds.user.domain.User
import com.rds.user.domain.UserState
import com.rds.user.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@IntegrationTest
class StudyRoomJoinConcurrencyTest @Autowired constructor(
    val studyRoomService: StudyRoomService,
    val studyRoomRepository: StudyRoomRepository,
    val userStudyRoomRepository: UserStudyRoomRepository,
    val categoryRepository: CategoryRepository,
    val userRepository: UserRepository,
    val studyRoomServiceLockFacade: StudyRoomServiceLockFacade
) {

    val fixture = kotlinFixture()

    @Test
    fun studyRoomJoinConcurrencyTest() {
        //given
        val threadCount = 50

        val category = categoryRepository.save(fixture<Category>())
        val studyRoom = studyRoomRepository.save(fixture<StudyRoom>() {
        property(StudyRoom::category) { category }
        property(StudyRoom::state) { StudyRoomState.ACTIVATED }
        })
        val studyRoomId = studyRoom.id
        val users = (1..threadCount).map { userRepository.save(fixture<User> { property(User::state) { UserState.ACTIVATED } }) }

        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (user in users) {
            executorService.execute {
                val response = studyRoomService.joinStudyRoom(studyRoomId, user.id)
                println(">>> response: $response")
                latch.countDown()
            }
        }
        latch.await()

        //then
        val userCount = userStudyRoomRepository.countByStudyRoom(studyRoomId)
        Assertions.assertThat(userCount).isGreaterThan(10)
    }

    @Test
    fun studyRoomJoinConcurrencyRedissonTest() {
        //given
        val threadCount = 100

        val category = categoryRepository.save(fixture<Category>())
        val studyRoom = studyRoomRepository.save(fixture<StudyRoom>() {
            property(StudyRoom::category) { category }
            property(StudyRoom::state) { StudyRoomState.ACTIVATED }
        })
        val studyRoomId = studyRoom.id
        val users = (1..threadCount).map { userRepository.save(fixture<User> { property(User::state) { UserState.ACTIVATED } }) }

        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (user in users) {
            executorService.execute {
                val response = studyRoomServiceLockFacade.joinStudyRoom(studyRoomId, user.id)
                println(">>> response: $response")
                latch.countDown()
            }
        }
        latch.await()
        Thread.sleep(5000)

        //then
        val userCount = userStudyRoomRepository.countByStudyRoom(studyRoomId)
        Assertions.assertThat(userCount).isEqualTo(10)
    }
}