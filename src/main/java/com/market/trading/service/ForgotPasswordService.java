package com.market.trading.service;

import com.market.trading.domain.VerificationType;
import com.market.trading.model.ForgotPassword;
import com.market.trading.model.User;
import jakarta.mail.MessagingException;

public interface ForgotPasswordService {

    public String generateResetToken(String userEmail) throws MessagingException;
    public String resetPassword(String resetToken, String newPassword);

}
