package com.market.trading.service;

import com.market.trading.domain.VerificationType;
import com.market.trading.model.ForgotPassword;
import com.market.trading.model.User;
import com.market.trading.repository.ForgotPasswordRepository;
import com.market.trading.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public String generateResetToken(String userEmail) throws MessagingException {

        Optional<ForgotPassword> existingRequest = forgotPasswordRepository.findByUserEmail(userEmail);
        if (existingRequest.isPresent() || userRepository.findByEmail(userEmail) == null) {
            return "A reset request already exists for this email/ enter a valid email address.";
        }
        String token = UUID.randomUUID().toString();
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setUserEmail(userEmail);
        forgotPassword.setResetToken(token);
        forgotPassword.setTokenExpiryTime(LocalDateTime.now().plusMinutes(10));
        forgotPasswordRepository.save(forgotPassword);

        emailService.sendVerificationOtpEmail(userEmail,token);

        return "Password reset token generated successfully ";
    }

    @Override
    public String resetPassword(String resetToken, String newPassword) {
        Optional<ForgotPassword> forgotPassword = forgotPasswordRepository.findByResetToken(resetToken);
        if (forgotPassword.isEmpty() || forgotPassword.get().getTokenExpiryTime().isBefore(LocalDateTime.now()) ) {
            return "Invalid or expired reset token.";
        }
        String request = forgotPassword.get().getUserEmail();
        User user = userRepository.findByEmail(request);
//        if(user.getPassword().equals(newPassword)){
//            return "Cannot use old Password.";
//        }
        user.setPassword(newPassword);
        userRepository.save(user);

        forgotPasswordRepository.delete(forgotPassword.get());  // Invalidate the reset request
        return "Password reset successfully.";
    }

}
