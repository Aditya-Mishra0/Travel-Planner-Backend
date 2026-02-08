package com.aditya.Travel_Planner.repository;

import com.aditya.Travel_Planner.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City,Long> {
    Optional<City> findByNameIgnoreCase(String name) ;
    List<City> findByNameContainingIgnoreCase(String name);
}
