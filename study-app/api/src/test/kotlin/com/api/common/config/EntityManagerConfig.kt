package com.api.common.config

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class EntityManagerConfig {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Bean
    fun entityManager(): EntityManager {
        return entityManager
    }
}