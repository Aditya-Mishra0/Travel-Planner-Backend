package com.aditya.Travel_Planner.service;

import com.aditya.Travel_Planner.model.Trip;
import com.aditya.Travel_Planner.model.TripStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TripService {
    Trip addTrip(Long userId, Trip trip, MultipartFile imageFile) ;
    List<Trip> getAllTripsByUser(Long userId) ;
    void deleteTrip(Long userId, Long id) ;
    Trip getTripById(Long userId, Long id) ;
    Trip updateTrip(Long userId, Long id, Trip tripDetails, MultipartFile imageFile) ;
    List<Trip> getTripByStatus(Long userId, TripStatus tripStatus) ;
    List<Trip> searchTripByName(Long userId, String city) ;

}
