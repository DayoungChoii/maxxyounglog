package com.api.auth.service

import com.api.auth.dto.LogInRequest
import com.api.auth.dto.LogInResponse
import com.api.auth.service.session.SessionLoginServiceImpl
import com.api.auth.constant.LogInStatus.*
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.user.domain.User
import com.rds.user.repository.UserRepository
import com.redis.auth.domain.Session
import com.redis.auth.repository.SessionRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class SessionLoginServiceImplTest :BehaviorSpec({

    val encryptor: Encryptor by lazy { mockk<Encryptor>() }
    val userRepository: UserRepository by lazy { mockk<UserRepository>() }
    val sessionRepository: SessionRepository by lazy { mockk<SessionRepository>() }
    val fixture by lazy { kotlinFixture() }
    val sessionLogInServiceImpl: SessionLoginServiceImpl by lazy { SessionLoginServiceImpl(encryptor, userRepository, sessionRepository) }

    `given`("로그인 할 때") {
        val request = fixture<LogInRequest>()
        `when`("이메일이 올바르지 않으면") {
            every { userRepository.findByEmail(request.email) } returns null
            `then`("유효하지 않은 이메일 상태를 반환한다.") {
                sessionLogInServiceImpl.login(request) shouldBe LogInResponse(INVALID_EMAIL)
            }
        }

        `when`("패스워드가 맞지 않으면") {
            every { userRepository.findByEmail(request.email) } returns fixture<User>()
            every { encryptor.matches(request.password, any(String::class)) } returns false
            `then`("유효하지 않은 비밀번호 상태를 반환한다.") {
                sessionLogInServiceImpl.login(request) shouldBe LogInResponse(INVALID_PASSWORD)
            }
        }

        `when`("로그인 정상처리 되면") {
            val savedSession: Session = fixture<Session>()
            every { userRepository.findByEmail(request.email) } returns fixture<User>()
            every { encryptor.matches(request.password, any(String::class)) } returns true
            every {sessionRepository.save(any(Session::class))} returns savedSession
            `then`("성공 상태와 세션 토큰을 반환한다.") {
                sessionLogInServiceImpl.login(request) shouldBe LogInResponse(SUCCESS, savedSession.token)

            }
        }
    }
})