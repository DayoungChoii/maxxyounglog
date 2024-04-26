package com.api.studyroom.service

import com.api.common.IntegrationTest
import com.api.common.RedisLockExecutor
import com.api.studyroom.repository.StudyRoomQueryRepository
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.category.domain.Category
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomState
import com.rds.studyroom.event.StudyRoomJoinedEvent
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.studyroom.repository.UserStudyRoomRepository
import com.rds.user.domain.User
import com.rds.user.domain.UserState
import com.rds.user.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@IntegrationTest
class StudyRoomJoinConcurrencyTest @Autowired constructor(
    val studyRoomRepository: StudyRoomRepository,
    val userStudyRoomRepository: UserStudyRoomRepository,
    val categoryRepository: CategoryRepository,
    val userRepository: UserRepository,
    val studyRoomJoinValidator: StudyRoomJoinValidator,
    val redisLockExecutor: RedisLockExecutor

) {

    val eventPublisher: ApplicationEventPublisher = mockk<ApplicationEventPublisher>()
    val studyRoomQueryRepository = mockk<StudyRoomQueryRepository>()
    val fixture = kotlinFixture()
    val threadCount = 50


    @Test
    fun studyRoomJoinConcurrencyTest() {
        //given
        val studyRoomService = createStudyRoomService()
        every { eventPublisher.publishEvent(any(StudyRoomJoinedEvent::class)) } returns Unit

        val (studyRoomId, users) = saveEntitiesForStudyRoom()
        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (user in users) {
            executorService.execute {
                studyRoomService.joinStudyRoom(studyRoomId, user.id)
                latch.countDown()
            }
        }
        latch.await()
        Thread.sleep(5000)

        //then
        val userCount = userStudyRoomRepository.countByStudyRoom(studyRoomId)
        Assertions.assertThat(userCount).isGreaterThanOrEqualTo(10)
    }

    private fun createStudyRoomService() = StudyRoomService(
        studyRoomRepository = studyRoomRepository,
        categoryRepository = categoryRepository,
        studyRoomQueryRepository = studyRoomQueryRepository,
        studyRoomJoinValidator = studyRoomJoinValidator,
        userStudyRoomRepository = userStudyRoomRepository,
        userRepository = userRepository,
        eventPublisher = eventPublisher
    )

    @Test
    fun studyRoomJoinConcurrencyRedissonTest() {
        //given
        val studyRoomServiceLockFacade = StudyRoomServiceLockFacade(
            studyRoomService = createStudyRoomService(),
            redisLockExecutor = redisLockExecutor
        )
        every { eventPublisher.publishEvent(any(StudyRoomJoinedEvent::class)) } returns Unit

        val (studyRoomId, users) = saveEntitiesForStudyRoom()
        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (user in users) {
            executorService.execute {
                studyRoomServiceLockFacade.joinStudyRoom(studyRoomId, user.id)
                latch.countDown()
            }
        }
        latch.await()
        Thread.sleep(5000)

        //then
        val userCount = userStudyRoomRepository.countByStudyRoom(studyRoomId)
        Assertions.assertThat(userCount).isEqualTo(10)
    }
    private fun saveEntitiesForStudyRoom(): Pair<Long, List<User>> {
        val category = categoryRepository.save(fixture<Category>())
        val studyRoom = studyRoomRepository.save(fixture<StudyRoom>() {
            property(StudyRoom::category) { category }
            property(StudyRoom::state) { StudyRoomState.ACTIVATED }
        })
        val studyRoomId = studyRoom.id
        val users =
            (1..threadCount).map { userRepository.save(fixture<User> { property(User::state) { UserState.ACTIVATED } }) }
        return Pair(studyRoomId, users)
    }

}