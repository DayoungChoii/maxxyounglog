package com.api.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import java.time.Duration


private const val PREFIX_OF_CACHE = "API::"
private const val LONG_TERM_CACHE = "longTerm"
private const val SHORT_TERM_CACHE = "shortTerm"


@Configuration
class CacheConfig {
    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .prefixCacheNameWith(PREFIX_OF_CACHE)
            .entryTtl(Duration.ofMinutes(1))
            .disableCachingNullValues()
            .serializeKeysWith(keySerializer)
            .serializeValuesWith(valueSerializer)
    }

    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer? {
        return RedisCacheManagerBuilderCustomizer { builder: RedisCacheManagerBuilder ->
            builder
                .withCacheConfiguration(
                    SHORT_TERM_CACHE,
                    RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(1))
                        .disableCachingNullValues()
                        .serializeKeysWith(keySerializer)
                        .serializeValuesWith(valueSerializer)
                )
                .withCacheConfiguration(
                    LONG_TERM_CACHE,
                    RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(2))
                        .disableCachingNullValues()
                        .serializeKeysWith(keySerializer)
                        .serializeValuesWith(valueSerializer)
                )
        }
    }

    private val keySerializer
        get() = RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())
    private val valueSerializer
        get() = RedisSerializationContext.SerializationPair.fromSerializer(
            GenericJackson2JsonRedisSerializer(
                redisObjectMapper
            )
        )

    private val redisObjectMapper
        get() = ObjectMapper()
            .registerModules(KotlinModule.Builder().build(), JavaTimeModule())
            .activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder().allowIfBaseType(Any::class.java).build(),
                ObjectMapper.DefaultTyping.EVERYTHING)

}