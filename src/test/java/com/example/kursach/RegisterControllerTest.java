package com.example.kursach;

import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.kursach.controllers.RegisterController;
import com.example.kursach.models.User;
import com.example.kursach.security.PasswordUtil;
import com.example.kursach.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private RegisterController registrationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowRegistration() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("/register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", isA(User.class)));
    }

    @Test
    public void testRegister_successfulRegistration() throws Exception {
        String username = "meow"; // ещё не использованное имя пользователя
        String password = "meow-meow-123";
        String confirmPassword = "meow-meow-123";

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordUtil.hashPassword(password));

        when(userService.findByUsername(username)).thenReturn(null); // пользователь не найден
        when(passwordUtil.checkPassword(confirmPassword, user.getPassword())).thenReturn(true);

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("confirmPassword", confirmPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testRegister_wrongUsername() throws Exception {
        String username = "ya"; // уже используемое имя пользователя
        String password = "253536226"; // не используемый пароль

        when(userService.findByUsername(username)).thenReturn(null); // пользователь найден

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("confirmPassword", password))
                .andExpect(status().isOk()) // Ожидаем успешный ответ (200 OK)
                .andExpect(view().name("/register")) // Ожидаем возврат на страницу логина
                .andExpect(model().attributeHasErrors("user")) // Проверяем наличие ошибок в модели
                .andExpect(model().attribute("org.springframework.validation.BindingResult.user", notNullValue()))
                .andExpect(model().attributeHasFieldErrors("user", "username")); // Проверка ошибок для поля username
    }

    @Test
    public void testRegister_wrongConfirmPassword() throws Exception {
        String username = "no_existing_user";
        String password = "123";
        String confirmPassword = "1238656895";

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordUtil.hashPassword(password));

        when(userService.findByUsername(username)).thenReturn(null); // пользователь не найден
        when(passwordUtil.checkPassword(confirmPassword, user.getPassword())).thenReturn(false);

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("confirmPassword", confirmPassword))
                .andExpect(status().isOk()) // Ожидаем успешный ответ (200 OK)
                .andExpect(view().name("/register")) // Ожидаем возврат на страницу логина
                .andExpect(model().attributeHasErrors("user")) // Проверяем наличие ошибок в модели
                .andExpect(model().attribute("org.springframework.validation.BindingResult.user", notNullValue()))
                .andExpect(model().attributeHasFieldErrors("user", "passwordConfirm")); // Проверка ошибок для поля passwordConfirm
    }
}
