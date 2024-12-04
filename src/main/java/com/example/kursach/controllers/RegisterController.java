package com.example.kursach.controllers;

import com.example.kursach.models.User;
import com.example.kursach.security.PasswordUtil;
import com.example.kursach.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordUtil passwordUtil;


    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "/register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User userRegister,
                               BindingResult bindingResult,
                               Model model) {
        if (userService.existsByUsername(userRegister.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Пользователь с таким именем уже существует");
        }

        if (!userRegister.getPassword().equals(userRegister.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "error.passwordConfirm", "Пароли не совпадают");
        }

        if (bindingResult.hasErrors()) {
            return "/register";
        }

        // Хешируем пароль
        String hashedPassword = passwordUtil.hashPassword(userRegister.getPassword());
        userService.saveUser(userRegister.getUsername(), hashedPassword);
        return "redirect:/login";
    }
}
