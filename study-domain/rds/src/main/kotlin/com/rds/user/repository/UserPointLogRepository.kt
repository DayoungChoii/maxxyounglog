package com.rds.user.repository

import com.rds.user.domain.UserPointLog
import org.springframework.data.jpa.repository.JpaRepository

interface UserPointLogRepository: JpaRepository<UserPointLog, Long> {
}