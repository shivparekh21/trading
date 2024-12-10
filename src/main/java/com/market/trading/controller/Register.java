package com.market.trading.controller;

import com.market.trading.config.JwtProvider;
import com.market.trading.model.User;
import com.market.trading.repository.UserRepository;
import com.market.trading.response.JwtResponse;
import com.market.trading.service.CustomUserServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class Register {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserServiceDetails customUserServiceDetails;

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


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User user) throws Exception {

        String userEmail = user.getEmail();
        String password = user.getPassword();

        //Authenticates the registered user by creating a UsernamePasswordAuthenticationToken.
        //Sets this authentication into the current SecurityContext.
        Authentication auth = authenticate(userEmail, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwtToken = JwtProvider.generateToken(auth);
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwtToken(jwtToken);
        jwtResponse.setStatus(true);
        jwtResponse.setMessage("Successfully login");

        return ResponseEntity.ok(jwtResponse);
    }

    private Authentication authenticate(String userEmail, String password) {
        UserDetails userDetails = customUserServiceDetails.loadUserByUsername(userEmail);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid email");
        }
        if(!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
