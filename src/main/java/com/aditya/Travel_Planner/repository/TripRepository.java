package com.aditya.Travel_Planner.repository;

import com.aditya.Travel_Planner.model.Trip;
import com.aditya.Travel_Planner.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Long> {
    List<Trip> findByUserId(Long userId) ;
    List<Trip> findByCityCityId(Long cityId) ;
    List<Trip> findByUserIdAndStatus(Long userId, TripStatus status);
    List<Trip> findByUserIdAndCityNameContainingIgnoreCase(Long userId, String cityName);
}
