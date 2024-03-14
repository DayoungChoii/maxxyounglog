package com.api.auth.controller

import com.api.auth.dto.LogInRequest
import com.api.auth.dto.LogInResponse
import com.api.auth.service.LoginService
import com.api.auth.status.LogInStatus
import com.api.auth.status.SignUpStatus
import com.appmattus.kotlinfixture.kotlinFixture
import com.google.gson.Gson
import com.redis.auth.domain.Session
import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class LogInControllerTest: FunSpec () {
    private val loginService: LoginService by lazy{ mock(LoginService::class.java) }
    private val loginController: LoginController by lazy { LoginController(loginService, "localhost") }
    val fixture by lazy { kotlinFixture() }
    private val mockMvc: MockMvc by lazy{ MockMvcBuilders.standaloneSetup(loginController).build()}


    init {
        context("로그인 할 때 ") {
            val request = fixture<LogInRequest>()
            test("정상 처리가 되면 body에 SUCCESS header에 토큰값이 들어있다.") {
                val savedSession: Session = fixture<Session>()
                `when`(loginService.login(request)).thenReturn(LogInResponse(LogInStatus.SUCCESS, savedSession.token))

                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Gson().toJson(request))
                )
                resultActions
                    .andExpect(status().isOk)
                    .andExpect(cookie().value("SESSION", savedSession.token))
                    .andExpect(jsonPath("$.status").value(SignUpStatus.SUCCESS.name))
            }

        }

    }

}