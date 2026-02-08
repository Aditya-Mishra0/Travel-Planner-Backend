package com.aditya.Travel_Planner.controller;

import com.aditya.Travel_Planner.model.Trip;
import com.aditya.Travel_Planner.service.TripService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService ;
    private final ObjectMapper objectMapper ;

    public TripController(TripService tripService, ObjectMapper objectMapper) {
        this.tripService = tripService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/user/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Trip> addTrip(
            @PathVariable Long userId,
            @RequestPart("trip") String tripJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws Exception {

        Trip trip = objectMapper.readValue(tripJson, Trip.class);

        Trip savedTrip = tripService.addTrip(userId, trip, imageFile);
        return ResponseEntity.status(201).body(savedTrip);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Trip>> getAllTrips(@PathVariable Long userId){
        return ResponseEntity.ok(tripService.getAllTripsByUser(userId)) ;
    }

    @PutMapping(value = "/{id}/user/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Trip> updateTrip(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestPart("trip") String tripJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws Exception {

        Trip tripDetails = objectMapper.readValue(tripJson, Trip.class);

        Trip updatedTrip = tripService.updateTrip(userId, id, tripDetails, imageFile);
        return ResponseEntity.ok(updatedTrip);
    }

    @DeleteMapping("/user/{userId}/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long userId , @PathVariable Long id){
        tripService.deleteTrip(userId,id);
        return ResponseEntity.noContent().build();
    }


}
