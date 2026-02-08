package com.aditya.Travel_Planner.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @NotBlank(message = "Name cannot be blank")
    private String name ;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email cannot be blank")
    @Column(unique = true, nullable = false)
    private String email ;

    @NotBlank(message = "Enter a password")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password ;

}
