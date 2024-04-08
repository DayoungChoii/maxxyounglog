package com.api.studyroom.repository

import com.api.studyroom.dto.StudyRoomSearch
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rds.category.domain.Category
import com.rds.category.domain.QCategory.category
import com.rds.studyroom.domain.QStudyRoom.studyRoom
import com.rds.studyroom.domain.StudyRoom
import org.springframework.stereotype.Repository

@Repository
class StudyRoomQueryRepository (
    private val queryFactory: JPAQueryFactory
) {
    fun findStudyRoomList (
        studyRoomSearch: StudyRoomSearch,
        studyRoomId: Long?,
        pageSize: Long
    ): List<StudyRoom>? {
        return queryFactory
            .selectFrom(studyRoom)
            .innerJoin(studyRoom.category, category).fetchJoin()
            .where(
                studyRoomIdLt(studyRoomId),
                titleLike(studyRoomSearch.title),
                categoryEq(studyRoomSearch.category)
            )
            .orderBy(studyRoom.id.desc())
            .limit(pageSize)
            .fetch()
    }

    private fun studyRoomIdLt(studyRoomId: Long?): BooleanExpression? =
        studyRoomId?.let { studyRoom.id.lt(it) }

    private fun titleLike(title: String?): BooleanExpression? =
        title?.let { studyRoom.title.like("$title%")}

    private fun categoryEq(category: Category?): BooleanExpression? =
        category?.let { studyRoom.category.eq(category) }





}