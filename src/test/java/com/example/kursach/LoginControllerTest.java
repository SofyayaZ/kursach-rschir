package com.example.kursach;

import com.example.kursach.DTO.UserDTO;
import com.example.kursach.models.User;
import com.example.kursach.security.PasswordUtil;
import com.example.kursach.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PasswordUtil passwordUtil;


    @Test
    public void testLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(view().name("/login"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", instanceOf(UserDTO.class)));
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        // Создаем объект пользователя для успешного входа
        String username = "user";
        String password = "pass";

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordUtil.hashPassword(password)); // Хешированный корректный пароль

        when(userService.findByUsername(username)).thenReturn(user);
        when(passwordUtil.checkPassword(password, user.getPassword())).thenReturn(true);

        // Выполнение POST-запроса с правильными данными
        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(redirectedUrl("/lmap")); // Ожидаем редирект на "/lmap"
    }

    @Test
    public void testLoginUser_UserNotFound() throws Exception {
        String username = "unknownUser"; // Пользователь, который не существует
        String password = "whateverPassword"; // Любой пароль, он не будет использован

        // Настраиваем моки
        when(userService.findByUsername(username)).thenReturn(null); // Пользователь не найден

        // Выполняем POST-запрос с введенными данными
        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk()) // Ожидаем успешный ответ (200 OK)
                .andExpect(model().attributeExists("org.springframework.validation.BindingResult.user")) // Проверка наличия атрибута "BindingResult"
                .andExpect(model().attributeHasFieldErrors("user", "username")); // Проверка ошибок для поля username}
    }

    @Test
    public void testLoginUser_WrongPassword() throws Exception {
        // Создаем объект пользователя с неверным паролем
        String username = "user";
        String wrongPassword = "wrongPassword";
        String correctPassword = "correctPassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordUtil.hashPassword(correctPassword)); // Предполагаем, что это захешированный корректный пароль

        when(userService.findByUsername(username)).thenReturn(user); // Пользователь найден
        when(passwordUtil.checkPassword(wrongPassword, user.getPassword())).thenReturn(false); // Неверный пароль

        // Выполнение POST-запроса с неверным паролем
        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", wrongPassword))
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andExpect(view().name("/login")) // Ожидаем возврат на страницу логина
                .andExpect(model().attributeHasErrors("user")) // Проверяем наличие ошибок в модели
                .andExpect(model().attribute("org.springframework.validation.BindingResult.user", notNullValue()))
                .andExpect(model().attributeHasFieldErrors("user", "password")); // Проверяем ошибку для поля password
    }
}