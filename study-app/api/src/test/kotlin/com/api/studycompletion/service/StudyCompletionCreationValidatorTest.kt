package com.api.studycompletion.service

import com.api.studycompletion.constant.StudyCompletionCreationValidatorStatus
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomState
import com.rds.user.domain.User
import com.rds.user.domain.UserState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class StudyCompletionCreationValidatorTest:  BehaviorSpec({
    val fixture = kotlinFixture()

    `given`("스터디 인증 요청 검증 시") {
        val emptyStudyRoom = null
        val emptyUser = null
        val inValidStudyRoom = fixture<StudyRoom>() {
            property(StudyRoom::state) { StudyRoomState.DISABLED }
        }
        val invalidUser = fixture<User>() {
            property(User::state) { UserState.BLOCKED }
        }
        val validStudyRoom = fixture<StudyRoom>() {
        property(StudyRoom::state) { StudyRoomState.ACTIVATED }
        }
        val validUser = fixture<User>() {
            property(User::state) { UserState.ACTIVATED }
        }
        `when`("존재하지 않는 스터디방일 경우") {
            val validator = StudyCompletionCreationValidator(emptyStudyRoom, validUser)
            `then`("INVALID_ROOM 상태값을 반환한다.") {
                validator.validate() shouldBe  StudyCompletionCreationValidatorStatus.INVALID_ROOM
            }
        }
        `when`("비활성화 된 스터디방일 경우") {
            val validator = StudyCompletionCreationValidator(inValidStudyRoom, validUser)
            `then`("INVALID_ROOM 상태값을 반환한다.") {
                validator.validate() shouldBe  StudyCompletionCreationValidatorStatus.INVALID_ROOM
            }
        }
        `when`("존재하지 않는 유저일 경우") {
            val validator = StudyCompletionCreationValidator(validStudyRoom, emptyUser)
            `then`("INVALID_USER 상태값을 반환한다.") {
                validator.validate() shouldBe  StudyCompletionCreationValidatorStatus.INVALID_USER
            }
        }
        `when`("비활성화 된 유저일 경우") {
            val validator = StudyCompletionCreationValidator(validStudyRoom, invalidUser)
            `then`("INVALID_USER 상태값을 반환한다.") {
                validator.validate() shouldBe  StudyCompletionCreationValidatorStatus.INVALID_USER
            }
        }
    }

})
