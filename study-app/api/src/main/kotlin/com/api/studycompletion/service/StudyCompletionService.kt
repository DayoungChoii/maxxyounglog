package com.api.studycompletion.service

import com.api.common.file.FileUploader
import com.api.studycompletion.constant.StudyCompletionCreationStatus
import com.api.studycompletion.constant.StudyCompletionCreationValidatorStatus
import com.api.studycompletion.dto.StudyCompletionCreationRequest
import com.async.user.UserPointService
import com.rds.studyCompletion.domain.CompletionFile
import com.rds.studyCompletion.domain.StudyCompletion
import com.rds.studyCompletion.repository.CompletionFileRepository
import com.rds.studyCompletion.repository.StudyCompletionRepository
import com.rds.studyroom.repository.StudyRoomRepository
import com.rds.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class StudyCompletionService (
    private val studyCompletionRepository: StudyCompletionRepository,
    private val completionFileRepository: CompletionFileRepository,
    private val fileUploader: FileUploader,
    private val studyRoomRepository: StudyRoomRepository,
    private val userRepository: UserRepository,
    private val userPointService: UserPointService
) {

    @Transactional
    fun completeStudy(request: StudyCompletionCreationRequest): StudyCompletionCreationStatus{
        val studyRoom = studyRoomRepository.findByIdOrNull(request.studyRoomId)
        val user = userRepository.findByIdOrNull(request.userId)

        val validator = StudyCompletionCreationValidator(studyRoom, user)
        val validateResult = validator.validate()
        if (validateResult != StudyCompletionCreationValidatorStatus.SUCCESS) {
            return convertValidationToResponse(validateResult)
        }
        val fileName = fileUploader.upload(request.file)
        val savedFile = completionFileRepository.save(CompletionFile(fileName))

        studyCompletionRepository.save(
            StudyCompletion(
                studyRoom = studyRoom!!,
                user = user!!,
                completionFile = savedFile
            )
        )

        userPointService.addPoint(request.userId, 10)

        return StudyCompletionCreationStatus.SUCCESS

    }

    fun convertValidationToResponse(validatorStatus: StudyCompletionCreationValidatorStatus): StudyCompletionCreationStatus {
        return when(validatorStatus) {
            StudyCompletionCreationValidatorStatus.INVALID_ROOM -> StudyCompletionCreationStatus.INVALID_ROOM
            StudyCompletionCreationValidatorStatus.INVALID_USER -> StudyCompletionCreationStatus.INVALID_USER
            else -> StudyCompletionCreationStatus.SUCCESS
        }
    }
}

