package com.api.studyroom.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rds.category.domain.QCategory.category
import com.rds.studyroom.domain.QStudyRoom.studyRoom
import com.rds.studyroom.domain.StudyRoom
import org.springframework.stereotype.Repository

@Repository
class MyStudyRoomQueryRepository (
    private val queryFactory: JPAQueryFactory
) {

    fun findMyStudyRoomDetail (
        studyRoomId: Long
    ): StudyRoom?
        = queryFactory
            .selectFrom(studyRoom)
            .innerJoin(studyRoom.category, category).fetchJoin()
            .where(studyRoomIdEq(studyRoomId))
            .fetchOne()
    private fun studyRoomIdEq(studyRoomId: Long): BooleanExpression? =
        studyRoomId.let { studyRoom.id.eq(it) }
}