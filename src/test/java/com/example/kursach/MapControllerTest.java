package com.example.kursach;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import com.example.kursach.controllers.MapController;
import com.example.kursach.models.Place;
import com.example.kursach.models.User;
import com.example.kursach.services.CommentService;
import com.example.kursach.services.PlaceService;
import com.example.kursach.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import jakarta.servlet.http.HttpSession;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MapControllerTest.class)
public class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    @Mock
    private PlaceService placeService;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private MapController mapController; // замените на имя вашего контроллера

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = standaloneSetup(mapController).build();
    }

    @Test
    void showLMap_UserNotLoggedIn_ShouldRedirectToLogin() throws Exception {

        MockHttpSession session = new MockHttpSession();

        // Выполняем GET запрос к /lmap с пустой сессией
        mockMvc.perform(get("/lmap").session(session)) // Передаем MockHttpSession
                .andExpect(status().is3xxRedirection()) // Ожидаем перенаправления
                .andExpect(redirectedUrl("/login")); // Ожидаем перенаправление на страницу входа
    }
    @Test
    void showLMap_UserLoggedIn_ShouldReturnLMapView() throws Exception {
        // Создаем мок пользователя
        User  user = new User();
        user.setUsername("user");

        // Создаем новый MockHttpSession и добавляем в него атрибут "user"
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        List<Place> places = placeService.allPlaces();


        // Выполняем GET запрос к /lmap с заполненной сессией
        mockMvc.perform(get("/lmap").session(session)) // Передаем MockHttpSession
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(model().attributeExists("locations")) // Проверка на содержание "locations" в модели
                .andExpect(model().attribute("locations", places)) // Проверка, что locations содержит правильные места
                .andExpect(view().name("lmap")); // Проверка, что отображается нужная страница
    }

}
