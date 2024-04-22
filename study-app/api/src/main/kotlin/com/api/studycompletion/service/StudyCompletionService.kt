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
    private val validator: StudyCompletionCreationValidator,
    private val studyRoomRepository: StudyRoomRepository,
    private val userRepository: UserRepository,
    private val userPointService: UserPointService
) {

    @Transactional
    fun completeStudy(request: StudyCompletionCreationRequest): StudyCompletionCreationStatus{
        //유효성 체크
        val validateResult = validator.validate(request)
        if (validateResult != StudyCompletionCreationValidatorStatus.SUCCESS) {
            return convertValidationToResponse(validateResult)
        }
        //파일 업로드
        val fileName = fileUploader.upload(request.file)
        //파일 저장
        val savedFile = completionFileRepository.save(CompletionFile(fileName))
        //스터디 인증 저장
        val studyRoom = studyRoomRepository.findByIdOrNull(request.studyRoomId)!!
        val user = userRepository.findByIdOrNull(request.userId)!!
        studyCompletionRepository.save(
            StudyCompletion(
                studyRoom = studyRoom,
                user = user,
                completionFile = savedFile
            )
        )
        //카프카 인증 포인트 10포인트
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

