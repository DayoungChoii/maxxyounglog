package com.redis.auth.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.time.Duration
import java.util.*


@RedisHash("people")
class Session (
    val userId: Long,
    val token: String = UUID.randomUUID().toString(),
    @TimeToLive
    val expiration: Long = Duration.ofDays(30).seconds
) {
    @Id
    var id: Long? = null
}