package com.redis.auth.repository

import com.redis.auth.domain.Session
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository: CrudRepository<Session, Long>