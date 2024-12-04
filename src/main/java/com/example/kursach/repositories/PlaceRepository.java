package com.example.kursach.repositories;

import com.example.kursach.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findPlaceById(Long id);
}
