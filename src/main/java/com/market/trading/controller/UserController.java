package com.market.trading.controller;

import com.market.trading.domain.VerificationType;
import com.market.trading.model.User;
import com.market.trading.service.EmailService;
import com.market.trading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/api/users/profiles")
    public ResponseEntity<User> getUserProfileByJwt(@RequestHeader("Authorization") String jwtToken) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users/enable-2fa")
    public ResponseEntity<String> enableTwoFactorAuthentication(
            @RequestParam VerificationType verificationType) throws Exception {

        // Extract user email from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        userService.enableTwoFactorAuthentication(verificationType, user);
        return ResponseEntity.ok("Two-factor authentication enabled successfully");
    }



}
