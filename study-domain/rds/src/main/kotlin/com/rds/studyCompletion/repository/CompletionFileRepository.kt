package com.rds.studyCompletion.repository

import com.rds.studyCompletion.domain.CompletionFile
import org.springframework.data.jpa.repository.JpaRepository

interface CompletionFileRepository : JpaRepository<CompletionFile, Long>{
}