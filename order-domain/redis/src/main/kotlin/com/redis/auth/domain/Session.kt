package com.redis.auth.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import java.time.Duration
import java.util.*


@RedisHash("session")
class Session (
    @Id
    val id: Long,
    @Indexed
    val token: String = UUID.randomUUID().toString(),
    @TimeToLive
    val expiration: Long = Duration.ofDays(30).seconds
)