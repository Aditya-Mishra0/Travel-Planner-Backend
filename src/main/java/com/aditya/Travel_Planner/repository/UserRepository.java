package com.aditya.Travel_Planner.repository;

import com.aditya.Travel_Planner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email) ;

}
