package com.api.studyroom.repository

import com.api.studyroom.dto.StudyRoomSearch
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.category.domain.Category
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.domain.StudyRoom
import com.rds.studyroom.repository.StudyRoomRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class StudyRoomQueryRepositoryTest @Autowired constructor (
    val studyRoomQueryDslRepository: StudyRoomQueryRepository,
    val studyRoomRepository: StudyRoomRepository,
    val categoryRepository: CategoryRepository
) {

    val fixture = kotlinFixture()
    val pageSize = 10L

    @Test
    fun findStudyRoomListTest() {
        // given
        saveStudyRoomList(fixture<Category>(), "testTitle")

        // when
        val findStudyRoomList = studyRoomQueryDslRepository.findStudyRoomList(StudyRoomSearch(null, null), null, pageSize)

        // then
        Assertions.assertThat(findStudyRoomList).hasSize(pageSize.toInt())
    }

    @Test
    fun findStudyRoomListSearchTest() {
        // given
        val category = fixture<Category>()
        val title = "testTitle"
        saveStudyRoomList(category, title)

        // when
        val findStudyRoomList = studyRoomQueryDslRepository.findStudyRoomList(StudyRoomSearch(title, category), null, pageSize)

        // then
        Assertions.assertThat(findStudyRoomList).hasSize(pageSize.toInt())
    }

    private fun saveStudyRoomList(category: Category, title: String) {
        categoryRepository.save(category)

        val studyRoomList = ArrayList<StudyRoom>()
        for (i in 1..100) {
            val studyRoom = fixture<StudyRoom>() {
                property(StudyRoom::category) { category }
                property(StudyRoom::title) {title}
            }
            studyRoomList.add(studyRoom)
        }
        studyRoomRepository.saveAll(studyRoomList)
    }


}