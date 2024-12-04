package com.example.kursach.controllers;

import com.example.kursach.DTO.UserDTO;
import com.example.kursach.models.User;
import com.example.kursach.security.PasswordUtil;
import com.example.kursach.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordUtil passwordUtil;


    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") UserDTO userLogin,
                            BindingResult bindingResult,
                            Model model,
                            HttpSession session) {
        User user = userService.findByUsername(userLogin.getUsername());

        // обработка ошибок
        if (user==null) {
            bindingResult.rejectValue("username", "error.username", "Пользователь не найден");
        } else if (!passwordUtil.checkPassword(userLogin.getPassword(), user.getPassword())) {
            bindingResult.rejectValue("password", "error.password", "Неверный пароль");
        }

        // возвращение на форму с выведенными ошибками
        if (bindingResult.hasErrors()) {
            return "/login";
        }

        // создание сессии для пользователя
        session.setAttribute("user", user);

        // переходим к карте с метками
        return "redirect:/lmap";
    }

}
