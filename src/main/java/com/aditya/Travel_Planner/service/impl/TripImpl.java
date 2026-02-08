package com.aditya.Travel_Planner.service.impl;

import com.aditya.Travel_Planner.model.City;
import com.aditya.Travel_Planner.model.Trip;
import com.aditya.Travel_Planner.model.TripStatus;
import com.aditya.Travel_Planner.model.User;
import com.aditya.Travel_Planner.repository.TripRepository;
import com.aditya.Travel_Planner.repository.UserRepository;
import com.aditya.Travel_Planner.service.CityService;
import com.aditya.Travel_Planner.service.TripService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TripImpl implements TripService {
    private final TripRepository tripRepository ;
    private final UserRepository userRepository ;
    private final CityService cityService ;
    private final ImageUpload imageUpload ;

    public TripImpl(TripRepository tripRepository, UserRepository userRepository, CityService cityService, ImageUpload imageUpload) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.cityService = cityService;
        this.imageUpload = imageUpload;
    }

    @Override
    public Trip addTrip(Long userId, Trip trip, MultipartFile imageFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        validateTrip(trip);

        if (trip.getCity() != null && trip.getCity().getName() != null) {
            City city = cityService.getOrCreateCity(trip.getCity().getName());
            trip.setCity(city);

            if (imageFile != null && !imageFile.isEmpty()) {
                trip.setImageUrl(imageUpload.uploadImage(imageFile));
            } else {
                trip.setImageUrl(city.getImageUrl());
            }
        }

        if (trip.getStatus() == TripStatus.TO_BE_VISITED) {
            trip.setRating(null);
            trip.setPersonalRemark(null);
        }
        trip.setUser(user);
        return tripRepository.save(trip);
    }

    @Override
    public List<Trip> getAllTripsByUser(Long userId) {
        return tripRepository.findByUserId(userId) ;
    }

    @Override
    public void deleteTrip(Long userId, Long id) {
        tripRepository.findById(id).ifPresent(t->{
            if (t.getUser().getId().equals(userId)){
                tripRepository.deleteById(id);
            }
        }) ;
    }

    @Override
    public Trip getTripById(Long userId, Long id) {
        return tripRepository.findById(id)
                .filter(t->t.getUser().getId().equals(userId))
                .orElseThrow(()->new RuntimeException("Trip not found")) ;
    }

    @Override
    public Trip updateTrip(Long userId, Long id, Trip tripDetails, MultipartFile imageFile) {
        return tripRepository.findById(id).map(exist -> {
            if (!exist.getUser().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized");
            }
            validateTrip(tripDetails);

            // Update basic info
            exist.setStartDate(tripDetails.getStartDate());
            exist.setEndDate(tripDetails.getEndDate());
            exist.setStatus(tripDetails.getStatus());

            if (tripDetails.getCity() != null && tripDetails.getCity().getName() != null) {
                String newName = tripDetails.getCity().getName();
                String oldName = (exist.getCity() != null) ? exist.getCity().getName() : "";

                if (!newName.equalsIgnoreCase(oldName)) {
                    City newCity = cityService.getOrCreateCity(newName);
                    exist.setCity(newCity);
                    if (imageFile == null || imageFile.isEmpty()) {
                        exist.setImageUrl(newCity.getImageUrl());
                    }
                }
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                exist.setImageUrl(imageUpload.uploadImage(imageFile));
            }

            if (tripDetails.getStatus() == TripStatus.VISITED) {
                exist.setRating(tripDetails.getRating());
                exist.setPersonalRemark(tripDetails.getPersonalRemark());
            } else {
                exist.setRating(null);
                exist.setPersonalRemark(null);
            }

            return tripRepository.save(exist);
        }).orElseThrow(() -> new RuntimeException("Trip not found"));
    }

    @Override
    public List<Trip> getTripByStatus(Long userId, TripStatus tripStatus) {
        return tripRepository.findByUserIdAndStatus(userId,tripStatus);
    }

    @Override
    public List<Trip> searchTripByName(Long userId, String city) {
        return tripRepository.findByUserIdAndCityNameContainingIgnoreCase(userId,city);
    }

    // logic to check trip and also set things to null if tbv
    private void validateTrip(Trip trip){
        if (trip.getStartDate() != null && trip.getEndDate() != null) {
            if (trip.getStartDate().isAfter(trip.getEndDate())) {
                throw new IllegalArgumentException("Start date cannot be after the end date.");
            }
        }

        if (trip.getStatus() == TripStatus.VISITED) {
            Integer rating = trip.getRating();
            if (rating != null && (rating < 1 || rating > 5)) {
                throw new IllegalArgumentException("Rating must be between 1 and 5.");
            }
        }

    }
}
