package com.api.auth.service

import com.api.auth.constant.SignUpStatus
import com.api.auth.dto.SignUpRequest
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.user.domain.User
import com.rds.user.event.UserSignedUpEvent
import com.rds.user.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.context.ApplicationEventPublisher

class SignUpServiceTest: BehaviorSpec({

    val userRepository: UserRepository by lazy { mockk<UserRepository>() }
    val encryptor: Encryptor by lazy { mockk<Encryptor>()}
    val eventPublisher: ApplicationEventPublisher by lazy { mockk<ApplicationEventPublisher>() }
    val signUpService: SignUpService by lazy { SignUpService(userRepository, encryptor, eventPublisher)}
    val fixture by lazy { kotlinFixture() }

    `given`("회원 가입할 때") {
        val request = fixture<SignUpRequest>()
        `when`("이메일이 중복이면") {
            every { userRepository.findByEmail(request.email) } returns fixture<User>()
            `then`("중복 상태를 반환한다.") {
                signUpService.signUp(request) shouldBe SignUpStatus.DUPLICATED_EMAIL
            }
        }

        `when`("정상 처리 되면") {
            every { userRepository.findByEmail(request.email) } returns null
            every { encryptor.encrypt(request.password) } returns "encryptedPassword"
            every { userRepository.save(any(User::class)) } returns fixture<User>()
            every { eventPublisher.publishEvent(any(UserSignedUpEvent::class)) } returns Unit
            `then`("완료 상태를 반환한다.") {
                signUpService.signUp(request) shouldBe SignUpStatus.SUCCESS
            }
        }
    }
})