package com.api.studyroom

import com.appmattus.kotlinfixture.kotlinFixture
import com.rds.category.domain.Category
import com.rds.studyroom.domain.StudyRoom

class StudyRoomProvider {
    companion object {
        fun createStudyRooms(category: Category, title: String, studyRoomNum: Int): ArrayList<StudyRoom> {
            val fixture = kotlinFixture()
            val studyRoomList = ArrayList<StudyRoom>()
            for (i in 1..studyRoomNum) {
                val studyRoom = fixture<StudyRoom>() {
                    property(StudyRoom::category) { category }
                    property(StudyRoom::title) { title + i }
                }
                studyRoomList.add(studyRoom)
            }
            return studyRoomList
        }
    }

}