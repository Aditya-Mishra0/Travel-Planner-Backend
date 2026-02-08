package com.aditya.Travel_Planner.service;

import com.aditya.Travel_Planner.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user) ;
    Optional<User> findByEmail(String email) ;
    Optional<User> findById(Long id) ;
    User updateProfile(User user,Long id) ;
    User loginUser(String email, String password) ;

}
