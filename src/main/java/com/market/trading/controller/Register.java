package com.market.trading.controller;

import com.market.trading.config.JwtProvider;
import com.market.trading.domain.VerificationType;
import com.market.trading.model.TwoFactorOtp;
import com.market.trading.model.User;
import com.market.trading.model.Wallet;
import com.market.trading.repository.UserRepository;
import com.market.trading.repository.WalletRepository;
import com.market.trading.response.AuthResponse;
import com.market.trading.service.CustomUserServiceDetails;
import com.market.trading.service.EmailService;
import com.market.trading.service.SmsService;
import com.market.trading.service.TwoFactorOtpService;
import com.market.trading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/register")
public class Register {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserServiceDetails customUserServiceDetails;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Autowired
    WalletRepository walletRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) throws Exception {

        User emailExists = userRepository.findByEmail(user.getEmail());
        if (emailExists != null) {
            throw new Exception("Email already exists");
        }

        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        User savedUser = userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);

        //Authenticates the registered user by creating a UsernamePasswordAuthenticationToken.
        //Sets this authentication into the current SecurityContext.
        Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwtToken = JwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("Successfully registered");

        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        String userEmail = user.getEmail();
        String password = user.getPassword();

        //Authenticates the registered user by creating a UsernamePasswordAuthenticationToken.
        //Sets this authentication into the current SecurityContext.
        Authentication auth = authenticate(userEmail, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        AuthResponse authResponse = new AuthResponse();

        User authenticatedUser = userRepository.findByEmail(userEmail);
        System.out.println("Login Email: " + authenticatedUser.getEmail());
        System.out.println("2FA Enabled: " + authenticatedUser.getTwoFactorAuth().isEnabled());
        // Check if the TwoFactorAuth is Enable for OTP
        if(authenticatedUser.getTwoFactorAuth().isEnabled()){
            authResponse.setMessage("Two-factor authentication required");
            authResponse.setTwoAuthEnabled(true);

            //Generate OTP
            String otp = OtpUtils.generateOtp();

            // If old OTP exists
            TwoFactorOtp oldTwoFactorOtp = twoFactorOtpService.findByUser(authenticatedUser.getId());
            if(oldTwoFactorOtp != null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }

            //New
            TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(
                    authenticatedUser, otp, null);

            //forwarding otp with email or mobile
            if (authenticatedUser.getTwoFactorAuth().getVerificationType() == VerificationType.EMAIL) {
                emailService.sendVerificationOtpEmail(userEmail, otp);
            } else if (authenticatedUser.getTwoFactorAuth().getVerificationType() == VerificationType.MOBILE) {
//                smsService.sendVerificationOtpSms(authenticatedUser.getTwoFactorAuth().getSendTo(), otp);
                smsService.sendVerificationOtpSms("+14704672435", otp);
            }

            authResponse.setSession(newTwoFactorOtp.getId());
            return ResponseEntity.accepted().body(authResponse);
        }


        String jwtToken = JwtProvider.generateToken(auth);
        authResponse.setJwtToken(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("Successfully login");

        return ResponseEntity.ok(authResponse);
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


    @PostMapping("/two-factor-verify/{otp}")
    public ResponseEntity<AuthResponse> verifyLoginOtp(@PathVariable String otp,
                                                       @RequestParam String id) {

        TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp, otp)) {

            Authentication auth = authenticate(twoFactorOtp.getUser().getEmail(), twoFactorOtp.getUser().getPassword());
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwtToken = JwtProvider.generateToken(auth);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two-factor authentication verified");
            authResponse.setTwoAuthEnabled(true);
            authResponse.setJwtToken(jwtToken);
            return ResponseEntity.ok(authResponse);
        }
        throw new BadCredentialsException("Invalid otp");
    }
}
