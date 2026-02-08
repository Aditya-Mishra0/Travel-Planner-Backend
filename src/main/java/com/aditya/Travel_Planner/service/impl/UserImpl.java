package com.aditya.Travel_Planner.service.impl;

import com.aditya.Travel_Planner.model.User;
import com.aditya.Travel_Planner.repository.UserRepository;
import com.aditya.Travel_Planner.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImpl implements UserService {
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder ;

    public UserImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository ;
        this.passwordEncoder = passwordEncoder ;
    }

    @Override
    public User registerUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("User already exists") ;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user) ;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateProfile(User user,Long id) {
        return userRepository.findById(id).map(existing ->{
            existing.setName(user.getName());
            return userRepository.save(existing) ;
        }).orElseThrow(()->new RuntimeException("User not found"));
    }

    @Override
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Invalid Email"))  ;
        if(passwordEncoder.matches(password,user.getPassword())){
            return user ;
        }else{
            throw new RuntimeException("Invalid Password") ;
        }
    }
}
