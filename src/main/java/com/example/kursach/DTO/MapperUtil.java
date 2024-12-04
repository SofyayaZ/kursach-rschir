package com.example.kursach.DTO;

import com.example.kursach.models.Comment;
import com.example.kursach.models.Place;
import com.example.kursach.models.User;

import java.util.List;
import java.util.stream.Collectors;


public class MapperUtil {

    public static PlaceDTO toPlaceDTO(Place place) {
        return new PlaceDTO(place.getId(),
                place.getName(),
                place.getLon(),
                place.getLat());
    }
}
