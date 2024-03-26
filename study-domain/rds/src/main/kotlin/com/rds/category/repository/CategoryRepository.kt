package com.rds.category.repository

import com.rds.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<Category, Long>