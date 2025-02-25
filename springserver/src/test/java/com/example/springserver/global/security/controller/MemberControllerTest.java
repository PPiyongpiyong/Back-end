package com.example.springserver.global.security.controller;

import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.security.dto.LoginRequestDto;
import com.example.springserver.global.security.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @MockBean
    private MemberService memberService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void successCreateAccount() throws Exception {
        //given

        //when

        //then

    }

    @DisplayName("회원이 이미 존재하는 경우")
    @Test
    void userAlreadyExists() throws Exception {}
}