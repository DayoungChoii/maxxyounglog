package com.crm.report.service

import com.crm.report.constant.ReportStatus
import com.crm.report.constant.ReportStatus.INVALID_USER
import com.crm.report.constant.ReportStatus.SUCCESS
import com.rds.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReportService (
    private val userRepository: UserRepository
) {

    @Transactional
    fun blockUser(userId: Long): ReportStatus {
        val user = userRepository.findByIdOrNull(userId) ?:  return INVALID_USER
        user.block()
        return SUCCESS
    }
}