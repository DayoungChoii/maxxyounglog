package com.api.auth.controller

import com.api.auth.dto.SignUpRequest
import com.api.auth.service.SignUpService
import com.api.auth.status.SignUpStatus
import com.google.gson.Gson
import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
internal class SignUpControllerTest: FunSpec() {

    private val signUpService: SignUpService by lazy{ mock(SignUpService::class.java)}

    private val signUpController: SignUpController by lazy{ SignUpController(signUpService)}

    private val mockMvc: MockMvc by lazy{ MockMvcBuilders.standaloneSetup(signUpController).build()}

    init {
        context("회원가입을 할 때") {
            test("정상처리 되면 SUCCESS를 반환한다") {
                val request = SignUpRequest("test@example.com", "password", "John Doe")
                `when`(signUpService.signUp(request)).thenReturn(SignUpStatus.SUCCESS)

                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Gson().toJson(request))
                )
                resultActions
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(SignUpStatus.SUCCESS.name))
            }
            test("파라미터에 공백이 있다면 응답 코드가 400이다.") {
                val request = SignUpRequest("", "password", "John Doe")

                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Gson().toJson(request))
                )
                resultActions
                    .andExpect(status().isBadRequest)
            }

        }
    }
}