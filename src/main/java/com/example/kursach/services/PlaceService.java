package com.example.kursach.services;

import com.example.kursach.models.Place;
import com.example.kursach.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {
    @Autowired
    private PlaceRepository placeRepository;

    public Place findPlaceById(Long id) {
        Optional<Place> placeOptional = Optional.ofNullable(placeRepository.findPlaceById(id));
        return placeOptional.orElse(null);
    }

    public List<Place> allPlaces() {
        return placeRepository.findAll();
    }
}
