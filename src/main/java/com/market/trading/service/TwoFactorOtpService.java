package com.market.trading.service;

import com.market.trading.model.TwoFactorOtp;
import com.market.trading.model.User;

public interface TwoFactorOtpService {

    TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt);
    TwoFactorOtp findByUser(Long userId);
    TwoFactorOtp findById(String id);
    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp);
    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}
