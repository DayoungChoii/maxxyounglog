package com.batch

import com.rds.studyroom.domain.QStudyRoom.*
import com.rds.studyroom.domain.QUserStudyRoom.*
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.domain.StudyRoomState
import jakarta.persistence.EntityManagerFactory
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager
import java.time.Instant


@Configuration
class StudyRoomDisabledBatch (
    private val jobLauncher: JobLauncher,
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,

    ) {

    companion object {
        const val JOB_NAME = "studyRoomDisabledBatch"
        const val CHUNK_SIZE = 1000
        const val EVERYDAY_END_OF_MONTH = "0 0 0 1 * *"
    }
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(cron = EVERYDAY_END_OF_MONTH)
    fun execute() {
        jobLauncher.run(
            job(jobRepository, transactionManager),
            JobParametersBuilder()
                .addString("date", Instant.now().toString())
                .toJobParameters()
        )
    }



    @Bean("${JOB_NAME}_job")
    fun job(jobRepository: JobRepository,
            transactionManager: PlatformTransactionManager
    ): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .start(step(jobRepository, transactionManager))
            .build()
    }

    @Bean("${JOB_NAME}_step")
    fun step(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("${JOB_NAME}_step", jobRepository)
            .chunk<StudyRoom, StudyRoom>(CHUNK_SIZE, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @StepScope
    @Bean("${JOB_NAME}_reader")
    fun reader(): QuerydslPagingItemReader<StudyRoom> {
        return QuerydslPagingItemReader(entityManagerFactory, CHUNK_SIZE) {
            it.selectFrom(studyRoom)
                .leftJoin(studyRoom.userStudyRooms, userStudyRoom)
                .where(
                    studyRoom.state.eq(StudyRoomState.ACTIVATED)
                )
                .groupBy(studyRoom.id)
                .having(userStudyRoom.id.count().eq(0))
        }
    }

    @Bean("${JOB_NAME}_processor")
    fun processor(): ItemProcessor<StudyRoom, StudyRoom> {
        return ItemProcessor<StudyRoom, StudyRoom> {
            it.apply { disabled() }
        }
    }

    @Bean("${JOB_NAME}_writer")
    fun writer(): JpaItemWriter<StudyRoom> {
        return JpaItemWriter<StudyRoom>()
            .apply {
                setEntityManagerFactory(entityManagerFactory)
            }
    }
}