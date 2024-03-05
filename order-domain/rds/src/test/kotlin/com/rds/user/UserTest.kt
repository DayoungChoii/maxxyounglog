package com.rds.user

import com.rds.user.domain.User
import com.rds.user.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserTest @Autowired constructor(
    val userRepository: UserRepository
){
    @Test
    fun `유저가 저장될 때 createdAt 컬럼의 값이 있다`() {
        //given
        val user = User(
            email = "maxxyoung.dev@gmail.com",
            password = "test",
            name = "maxxyoung"
        )

        //when
        userRepository.save(user)

        //then
        Assertions.assertThat(user.createdAt).isNotNull
    }

    @Test
    fun `유저가 수정될 때 updatedAt 컬럼의 값이 있다`() {
        //given
        val user = User(
            email = "maxxyoung.dev@gmail.com",
            password = "test",
            name = "maxxyoung"
        )

        //when
        userRepository.save(user)
        user.updatePassword("test1")

        //then
        Assertions.assertThat(user.updatedAt).isNotNull
    }
}