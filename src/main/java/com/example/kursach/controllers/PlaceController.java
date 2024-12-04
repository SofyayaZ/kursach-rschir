package com.example.kursach.controllers;

import com.example.kursach.DTO.MapperUtil;
import com.example.kursach.DTO.PlaceDTO;
import com.example.kursach.models.Place;
import com.example.kursach.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlaceController {
    @Autowired
    private PlaceService placeService;

    @GetMapping("/api/places")
    public List<PlaceDTO> getPlaces() {
        List<Place> places = placeService.allPlaces();
        List<PlaceDTO> placeDTOs = new ArrayList<>();
        for (Place place : places) {
            PlaceDTO placeDTO = MapperUtil.toPlaceDTO(place);
            placeDTOs.add(placeDTO);
        }
        return placeDTOs;
    }

}
