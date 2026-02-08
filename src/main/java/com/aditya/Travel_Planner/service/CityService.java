package com.aditya.Travel_Planner.service;

import com.aditya.Travel_Planner.model.City;

import java.util.List;

public interface CityService {
    City getOrCreateCity(String city) ;
    List<City> getCityByName(String city) ;
    List<City> getAllCities() ;
    List<City> searchCitiesFromApi(String cityName) ;
    City getCityById(Long id) ;

}
