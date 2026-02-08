package com.aditya.Travel_Planner.controller;

import com.aditya.Travel_Planner.model.User;
import com.aditya.Travel_Planner.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService ;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        return ResponseEntity.ok(userService.registerUser(user)) ;
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email){
        Optional<User> user = userService.findByEmail(email) ;
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable Long id, @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.updateProfile(user, id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try {
            User user1 = userService.loginUser(user.getEmail(),user.getPassword()) ;
            return ResponseEntity.ok(user1) ;
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }

    }



}
