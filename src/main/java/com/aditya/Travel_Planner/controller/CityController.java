package com.aditya.Travel_Planner.controller;

import com.aditya.Travel_Planner.model.City;
import com.aditya.Travel_Planner.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/search-api")
    public ResponseEntity<List<City>> searchApi(@RequestParam String name) {
        return ResponseEntity.ok(cityService.searchCitiesFromApi(name));
    }


    @PostMapping("/save")
    public ResponseEntity<City> saveCity(@RequestParam String fullName) {
        return ResponseEntity.ok(cityService.getOrCreateCity(fullName));
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }
}