package com.api.common

import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class DatabaseCleaner @Autowired constructor(
    private var entityManager: EntityManager
) {

    private val tableNames: List<String> = entityManager.metamodel.entities
        .filter { it.javaType.getAnnotation(Entity::class.java) != null }
        .map { it.name.toSnakeCase() }

    fun String.toSnakeCase(): String {
        return this.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").toLowerCase()
    }

    @Transactional
    fun execute() {
        entityManager.createNativeQuery("SET foreign_key_checks = 0").executeUpdate()

        for (tableName in tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName;").executeUpdate()
            entityManager.createNativeQuery("ALTER TABLE $tableName AUTO_INCREMENT = 1;").executeUpdate()
        }

        entityManager.createNativeQuery("SET foreign_key_checks = 1").executeUpdate()
    }
}