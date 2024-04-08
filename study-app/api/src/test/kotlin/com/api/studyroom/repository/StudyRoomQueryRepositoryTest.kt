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

@SpringBootTest
class StudyRoomQueryRepositoryTest @Autowired constructor (
    val studyRoomQueryDslRepository: StudyRoomQueryRepository,
    val studyRoomRepository: StudyRoomRepository,
    val categoryRepository: CategoryRepository
) {

    val fixture = kotlinFixture()

    @Test
    fun findStudyRoomListTest() {
        // given
        val category = fixture<Category>()
        categoryRepository.save(category)

        val studyRoomList = ArrayList<StudyRoom>()
        for(i in 1..100){
            val studyRoom = fixture<StudyRoom>() {
                property(StudyRoom::category) {category}
            }
            studyRoomList.add(studyRoom)
        }

        studyRoomRepository.saveAll(studyRoomList)

        // when
        val findStudyRoomList = studyRoomQueryDslRepository.findStudyRoomList(StudyRoomSearch(null, null), null, 10)

        // then
        Assertions.assertThat(findStudyRoomList).hasSize(10)
    }




}