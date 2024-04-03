package com.rds.user.repository

import com.rds.user.domain.UserPoint
import org.springframework.data.jpa.repository.JpaRepository

interface UserPointRepository: JpaRepository<UserPoint, Long> {
}