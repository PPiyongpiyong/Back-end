package com.example.springserver.api.Manual.Controller;

import com.example.springserver.api.Manual.Service.ManualService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.anyString;


@WebMvcTest(ManualControllerTest.class)
class ManualControllerTest {

    @MockBean
    private ManualService manualService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("")
    @Test
    void successAutoComplete() throws Exception {
        //given
        //when
        //then
    }
}