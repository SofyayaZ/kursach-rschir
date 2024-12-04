package com.example.kursach.controllers;

import com.example.kursach.models.Place;
import com.example.kursach.models.User;
import com.example.kursach.services.CommentService;
import com.example.kursach.services.PlaceService;
import com.example.kursach.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MapController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/lmap")
    public String showLMap(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        //перенаправить на страницу входа не авторизованного юзера
        if (user==null) {
            return "redirect:/login";
        }

        List<Place> places = placeService.allPlaces();
        model.addAttribute("locations", places);

        return "/lmap";
    }
}
