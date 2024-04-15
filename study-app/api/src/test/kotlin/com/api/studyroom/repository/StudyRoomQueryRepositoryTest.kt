package com.api.studyroom.repository

import com.api.common.IntegrationTest
import com.api.studyroom.StudyRoomProvider
import com.api.studyroom.dto.StudyRoomSearch
import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.category.domain.Category
import com.rds.category.repository.CategoryRepository
import com.rds.studyroom.repository.StudyRoomRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class StudyRoomQueryRepositoryTest @Autowired constructor (
    val studyRoomQueryDslRepository: StudyRoomQueryRepository,
    val studyRoomRepository: StudyRoomRepository,
    val categoryRepository: CategoryRepository
) {

    val fixture = kotlinFixture()
    val pageSize = 10L

    @Test
    fun findStudyRoomListTest_firstPage() {
        // given
        saveStudyRoomList(fixture<Category>(), "testTitle", 30)

        // when
        val findStudyRoomList = studyRoomQueryDslRepository.findStudyRoomList(StudyRoomSearch(null, null), null, pageSize)

        // then
        assertThat(findStudyRoomList).hasSize(pageSize.toInt())
        assertThat(findStudyRoomList!!.first().title).isEqualTo("testTitle30")
        assertThat(findStudyRoomList.last().title).isEqualTo("testTitle21")
    }

    @Test
    fun findStudyRoomListTest_secondPage() {
        // given
        saveStudyRoomList(fixture<Category>(), "testTitle", 30)

        // when
        val findStudyRoomList = studyRoomQueryDslRepository.findStudyRoomList(StudyRoomSearch(null, null), 21, pageSize)

        // then
        assertThat(findStudyRoomList).hasSize(pageSize.toInt())
        assertThat(findStudyRoomList!!.first().title).isEqualTo("testTitle20")
        assertThat(findStudyRoomList.last().title).isEqualTo("testTitle11")
    }

    @Test
    fun findStudyRoomListSearchTest() {
        // given
        val category = fixture<Category>()
        val title = "testTitle"
        saveStudyRoomList(category, title, 30)

        // when
        val findStudyRoomList = studyRoomQueryDslRepository.findStudyRoomList(StudyRoomSearch(title + "30", category.id), null, pageSize)

        // then
        assertThat(findStudyRoomList).hasSize(1)
        assertThat(findStudyRoomList!!.first().title).isEqualTo("testTitle30")
    }

    private fun saveStudyRoomList(category: Category, title: String, studyRoomNum: Int) {
        categoryRepository.save(category)
        val studyRoomList = StudyRoomProvider.createStudyRooms(category, title, studyRoomNum)
        studyRoomRepository.saveAll(studyRoomList)
    }

    @Test
    fun findStudyRoomDetailTest() {
        // given
        val category = fixture<Category>()
        val title = "testTitle"
        saveStudyRoomList(category, title, 30)

        // when
        val findStudyRoomDetail = studyRoomQueryDslRepository.findStudyRoomDetail(1L)

        // then
        assertThat(findStudyRoomDetail).isNotNull
        assertThat(findStudyRoomDetail!!.id).isEqualTo(1L)
    }
}