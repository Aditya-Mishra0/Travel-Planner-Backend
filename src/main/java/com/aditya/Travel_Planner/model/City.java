package com.aditya.Travel_Planner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId ;

    @NotBlank(message = "City name is required")
    private String name ;

    @NotBlank(message = "Country name is required")
    private String country ;

    @Column(columnDefinition = "TEXT")
    private String description ;

    private String imageUrl ;
}
