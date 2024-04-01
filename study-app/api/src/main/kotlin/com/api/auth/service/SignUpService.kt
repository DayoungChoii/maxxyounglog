package com.api.auth.service

import com.api.auth.constant.SignUpStatus
import com.api.auth.dto.SignUpRequest
import com.api.auth.exception.AuthException
import com.api.auth.exception.AuthExceptionType
import com.api.common.const.SIGN_UP_POINT
import com.async.user.UserPointService
import com.rds.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignUpService (
    private val userRepository: UserRepository,
    private val encryptor: Encryptor,
    private val userPointService: UserPointService
){

    @Transactional
    fun signUp(signUpRequest: SignUpRequest): SignUpStatus {
        if (isEmailDuplicated(signUpRequest.email)) {
            return SignUpStatus.DUPLICATED_EMAIL
        }
        try {
            val encryptedPassword = encryptor.encrypt(signUpRequest.password)
            signUpRequest.encryptPassword(encryptedPassword)
            val user = userRepository.save(signUpRequest.toUser());
            userPointService.createPoint(user.id, SIGN_UP_POINT)
        } catch (e: RuntimeException) {
            throw AuthException(AuthExceptionType.SIGN_UP_FAIL, e)
        }
        return SignUpStatus.SUCCESS;
    }

    private fun isEmailDuplicated(email: String): Boolean =
        userRepository.findByEmail(email) != null

}