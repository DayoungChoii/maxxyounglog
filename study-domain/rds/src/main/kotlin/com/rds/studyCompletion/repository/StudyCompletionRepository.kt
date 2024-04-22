package com.rds.studyCompletion.repository

import com.rds.studyCompletion.domain.StudyCompletion
import org.springframework.data.jpa.repository.JpaRepository

interface StudyCompletionRepository : JpaRepository<StudyCompletion, Long>{
}