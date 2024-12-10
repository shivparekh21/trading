package com.market.trading.controller;

import com.market.trading.config.JwtProvider;
import com.market.trading.model.User;
import com.market.trading.repository.UserRepository;
import com.market.trading.response.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class Register {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> registerUser(@RequestBody User user) throws Exception {

        User emailExists = userRepository.findByEmail(user.getEmail());
        if (emailExists != null) {
            throw new Exception("Email already exists");
        }

        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        User savedUser = userRepository.save(user);

        //Authenticates the registered user by creating a UsernamePasswordAuthenticationToken.
        //Sets this authentication into the current SecurityContext.
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwtToken = JwtProvider.generateToken(auth);
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwtToken(jwtToken);
        jwtResponse.setStatus(true);
        jwtResponse.setMessage("Successfully registered");

        return ResponseEntity.ok(jwtResponse);
    }
}
