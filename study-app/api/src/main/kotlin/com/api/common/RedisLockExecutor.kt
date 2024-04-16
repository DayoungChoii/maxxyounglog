package com.api.common

import com.api.common.exception.RedisException
import com.api.common.exception.RedisExceptionType
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisLockExecutor (
    private val redissonClient: RedissonClient
) {
    fun <R> execute(target:Long, task:() -> R): R {
        val lock = redissonClient.getLock("concurrency-lock-$target")

        try {
            val available = lock.tryLock(10, 1, TimeUnit.SECONDS)

            if (!available) {
                throw RedisException(RedisExceptionType.LOCK_TIME_OUT)
            }
            return task()
        } catch (e: InterruptedException) {
            throw RedisException(RedisExceptionType.LOCK_TIME_OUT, e)
        } finally {
            lock.unlock()
        }
    }
}