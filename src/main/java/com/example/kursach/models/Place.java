package com.example.kursach.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="place")
public class Place {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="placename")
    private String name;

    @Column(name="lon")
    private double lon;

    @Column(name="lat")
    private double lat;

    @OneToMany (mappedBy = "place", cascade = CascadeType.ALL)
    List<Comment> comments;

    public Place() {}

    public Place(Long id, String name, double lon, double lat) {
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
