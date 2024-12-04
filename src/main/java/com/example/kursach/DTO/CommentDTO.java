package com.example.kursach.DTO;

import com.example.kursach.models.Place;
import com.example.kursach.models.User;

public class CommentDTO {
    private Long id;
    private String text;
    private User user;
    private Place place;

    public CommentDTO(Long id, String text, User user, Place place) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public User getuser() {
        return user;
    }

    public void setuser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
