package com.example.kursach.controllers;

import com.example.kursach.models.Comment;
import com.example.kursach.models.Place;
import com.example.kursach.models.User;
import com.example.kursach.services.PlaceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.kursach.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    PlaceService placeService;

    @GetMapping("/comments")
    public String getCommentsForPlace(@RequestParam(value = "id") Long placeId,
                                      HttpSession httpSession,
                                      Model model) {
        User currentUser = (User) httpSession.getAttribute("user");
        //перенаправить на страницу входа не авторизованного юзера
        if (currentUser==null) {
            return "redirect:/login";
        }

        Place currentPlace = placeService.findPlaceById(placeId);

        List<Comment> comments = commentService.getCommentsByPlaceId(placeId);

        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment());
        model.addAttribute("place", currentPlace);
        model.addAttribute("user", currentUser);

        return "comments";
    }

    @PostMapping("/comments/add")
    public String addComment(@RequestParam("placeId") Long placeId,
                             @RequestParam("text") String text,
                             HttpSession httpSession,
                             Model model) {

        User currentUser = (User)httpSession.getAttribute("user");
        Place currentPlace = placeService.findPlaceById(placeId);

        model.addAttribute("user", currentUser);
        model.addAttribute("place", currentPlace);

        Comment newComment = new Comment();
        if (text!=null && !text.isEmpty()) {
            newComment.setText(text);
            newComment.setUser(currentUser);
            newComment.setPlace(currentPlace);
            commentService.saveComment(newComment);
        }

        return "redirect:/comments?id=" + placeId;
    }

    @PatchMapping("/comments/update")
    public String updateComment(@RequestParam("commentId") Long commentId,
                                @RequestParam("placeId") Long placeId,
                                @RequestParam("newText") String newText,
                                HttpSession httpSession) {
        User currentUser = (User)httpSession.getAttribute("user");

        Comment comment = commentService.getCommentById(commentId);

        if (!newText.isEmpty() &&
                comment.getUser().getUsername().equals(currentUser.getUsername())) {
            commentService.updateComment(commentId, newText);
        }

        return "redirect:/comments?id=" + placeId;
    }

    @DeleteMapping("/comments/delete")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                @RequestParam("placeId") Long placeId,
                                HttpSession httpSession) {
        User currentUser = (User)httpSession.getAttribute("user");

        Comment comment = commentService.getCommentById(commentId);

        if (comment!=null &&
                comment.getUser().getUsername().equals(currentUser.getUsername())) {
            commentService.deleteComment(commentId);
        }
        return "redirect:/comments?id=" + placeId;
    }
}
