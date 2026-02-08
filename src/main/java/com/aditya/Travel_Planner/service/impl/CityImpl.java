package com.aditya.Travel_Planner.service.impl;

import com.aditya.Travel_Planner.model.City;
import com.aditya.Travel_Planner.repository.CityRepository;
import com.aditya.Travel_Planner.service.CityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CityImpl implements CityService {

    @Value("${unsplash.api.key}")
    private String unsplashKey;

    @Value("${unsplash.base.url}")
    private String unsplashUrl;

    @Value("${weather.base.url}")
    private String weatherUrl ;

    @Value("${weather.api.key}")
    private String weatherKey ;


    private final CityRepository cityRepository;
    private final RestTemplate restTemplate;

    public CityImpl(CityRepository cityRepository, RestTemplate restTemplate) {
        this.cityRepository = cityRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public City getOrCreateCity(String cityName) {
        return cityRepository.findByNameIgnoreCase(cityName)
                .orElseGet(() -> {
                    City newCity = new City();
                    newCity.setName(cityName);

                    Map<String, String> cityData = fetchFromUnsplash(cityName);

                    newCity.setImageUrl(cityData.get("image"));
                    newCity.setDescription(cityData.get("description"));


                    newCity.setCountry(extractCountryFromSearchName(cityName));

                    return cityRepository.save(newCity);
                });
    }


    private String extractCountryFromSearchName(String fullPath) {
        if (fullPath.contains(",")) {
            String[] parts = fullPath.split(",");
            return parts[parts.length - 1].trim();
        }
        return fullPath;
    }


    @Override
    public List<City> searchCitiesFromApi(String cityName) {
        try {
            String url = weatherUrl + cityName + "&limit=5&appid=" + weatherKey;
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
            List<Map<String, Object>> results = response.getBody();

            if (results != null) {
                return results.stream().map(data -> {
                    City tempCity = new City();
                    String cityNameAttr = (String) data.get("name"); // e.g., "Paris"
                    String state = (String) data.get("state");
                    String countryCode = (String) data.get("country");
                    String countryName = new Locale("", countryCode).getDisplayCountry();

                    // Create a full name that is unique and descriptive
                    // e.g., "Paris, Texas, United States"
                    String fullName = (state != null) ?
                            String.format("%s, %s, %s", cityNameAttr, state, countryName) :
                            String.format("%s, %s", cityNameAttr, countryName);

                    tempCity.setName(fullName); // This is what the user clicks
                    tempCity.setCountry(countryName); // This is for your UI labels
                    return tempCity;
                }).toList();
            }
        } catch (Exception e) {
            System.err.println("Search failed: " + e.getMessage());
        }
        return List.of();
    }



    @Override
    public List<City> getCityByName(String city) {
        return cityRepository.findByNameContainingIgnoreCase(city);
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City getCityById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    private Map<String, String> fetchFromUnsplash(String cityName) {
        // Default Values
        String imageUrl = "https://images.unsplash.com/photo-1488646953014-85cb44e25828";
        String description = "Discover the wonders of " + cityName;

        try {
            String url = unsplashUrl + cityName + "&per_page=1";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Client-ID " + unsplashKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");

            if (results != null && !results.isEmpty()) {
                Map<String, Object> firstResult = results.get(0);

                //  Image
                Map<String, String> urls = (Map<String, String>) firstResult.get("urls");
                imageUrl = urls.get("regular");

                //  Description logic
                String mainDesc = (String) firstResult.get("description");
                String altDesc = (String) firstResult.get("alt_description");

                if (mainDesc != null) {
                    description = mainDesc;
                } else if (altDesc != null) {
                    description = altDesc;
                }
            }
        } catch (Exception e) {
            System.err.println("Unsplash Data Fetch Error: " + e.getMessage());
        }

        return Map.of(
                "image", imageUrl,
                "description", description
        );
    }




}