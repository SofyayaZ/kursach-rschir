package com.example.kursach.DTO;

import com.example.kursach.models.Comment;
import jakarta.persistence.*;

import java.util.List;

public class PlaceDTO {
    private Long id;
    private String name;
    private double lon;
    private double lat;

    public PlaceDTO(Long id, String name, double lon, double lat) {
        this.id = id;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

}
