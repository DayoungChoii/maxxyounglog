package com.rds.category.repository

import com.rds.category.domain.Category
import org.springframework.data.repository.CrudRepository

interface CategoryRepository: CrudRepository<Category, Long>