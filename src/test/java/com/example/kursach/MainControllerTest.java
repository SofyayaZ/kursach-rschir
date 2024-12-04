package com.example.kursach;

import com.example.kursach.controllers.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndexReturnsCorrectView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"));
    }

    @Test
    public void testLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/logout").session(session))
                // Проверяем, что статус ответа 302 (редирект)
                .andExpect(status().is3xxRedirection())
                // Проверяем, что происходит редирект на /login
                .andExpect(redirectedUrl("/login"));
    }
}
