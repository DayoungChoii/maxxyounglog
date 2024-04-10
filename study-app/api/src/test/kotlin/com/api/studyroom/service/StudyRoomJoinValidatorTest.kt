package com.api.studyroom.service

import com.api.studyroom.constant.StudyRoomJoinValidatorStatus
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomState
import com.rds.studyroom.domain.UserStudyRoom
import com.rds.studyroom.domain.UserStudyRoomState
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.studyroom.repository.UserStudyRoomRepository
import com.rds.user.domain.User
import com.rds.user.domain.UserState
import com.rds.user.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class StudyRoomJoinValidatorTest: BehaviorSpec({
    val studyRoomRepository: StudyRoomRepository = mockk<StudyRoomRepository>()
    val userRepository: UserRepository = mockk<UserRepository>()
    val userStudyRepository: UserStudyRoomRepository = mockk<UserStudyRoomRepository>()
    val studyRoomJoinValidator: StudyRoomJoinValidator = StudyRoomJoinValidator(studyRoomRepository, userRepository, userStudyRepository)
    val fixture = kotlinFixture()

    `given`("스터디방 가입을 하려고 할 때") {
        val studyRoomId = 1L
        val userId = 1L
        `when`("스터디방의 상태가 이용할 수 없는 상태면") {
            every {studyRoomRepository.findByIdOrNull(studyRoomId)} returns fixture<StudyRoom>(){
                property(StudyRoom::state){StudyRoomState.DISABLED} }
            `then`("INVALID_ROOM 상태값을 반환한다.") {
                studyRoomJoinValidator.validate(Pair(studyRoomId, userId)) shouldBe StudyRoomJoinValidatorStatus.INVALID_ROOM
            }
        }
        `when`("차단된 유저라면") {
            every {studyRoomRepository.findByIdOrNull(studyRoomId)} returns fixture<StudyRoom>(){
                property(StudyRoom::state){StudyRoomState.ACTIVATED} }
            every {userRepository.findByIdOrNull(userId)} returns fixture<User>(){
                property(User::state){UserState.BLOCKED} }
            `then`("BLOCKED_USER 상태값을 반환한다.") {
                studyRoomJoinValidator.validate(Pair(studyRoomId, userId)) shouldBe StudyRoomJoinValidatorStatus.BLOCKED_USER
            }
        }
        `when`("스터디방 시스템 탈퇴 이력이 있는 유저라면") {
            every {studyRoomRepository.findByIdOrNull(studyRoomId)} returns fixture<StudyRoom>(){
                property(StudyRoom::state){StudyRoomState.ACTIVATED} }
            every {userRepository.findByIdOrNull(userId)} returns fixture<User>(){
                property(User::state){UserState.ACTIVATED} }
            every {userStudyRepository.findByStudyRoomIdAndUserId(studyRoomId, userId)} returns fixture<UserStudyRoom>(){
                property(UserStudyRoom::state){UserStudyRoomState.BLOCKED} }
            `then`("BLOCKED_USER 상태값을 반환한다.") {
                studyRoomJoinValidator.validate(Pair(studyRoomId, userId)) shouldBe StudyRoomJoinValidatorStatus.KICKED_OUT_USER
            }
        }
        `when`("스터디방을 스스로 나간 유저라면") {
            every {studyRoomRepository.findByIdOrNull(studyRoomId)} returns fixture<StudyRoom>(){
                property(StudyRoom::state){StudyRoomState.ACTIVATED} }
            every {userRepository.findByIdOrNull(userId)} returns fixture<User>(){
                property(User::state){UserState.ACTIVATED} }
            every {userStudyRepository.findByStudyRoomIdAndUserId(studyRoomId, userId)} returns fixture<UserStudyRoom>(){
                property(UserStudyRoom::state){UserStudyRoomState.DISABLED} }
            `then`("BLOCKED_USER 상태값을 반환한다.") {
                studyRoomJoinValidator.validate(Pair(studyRoomId, userId)) shouldBe StudyRoomJoinValidatorStatus.RESIGNED_USER
            }
        }
    }
})