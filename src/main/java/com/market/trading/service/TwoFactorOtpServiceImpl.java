package com.market.trading.service;

import com.market.trading.model.TwoFactorOtp;
import com.market.trading.model.User;
import com.market.trading.repository.TwoFactorOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{

    @Autowired
    private TwoFactorOtpRepository twoFactorOtpRepository;

    @Override
    public TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt) {
        // generates a unique identifier
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        TwoFactorOtp twoFactorOtp = new TwoFactorOtp();
        twoFactorOtp.setId(id);
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setJwt(jwt);
        twoFactorOtp.setUser(user);
        return twoFactorOtpRepository.save(twoFactorOtp);
    }

    @Override
    public TwoFactorOtp findByUser(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOtp findById(String id) {
        Optional<TwoFactorOtp> twoFactorOtp = twoFactorOtpRepository.findById(id);
        return twoFactorOtp.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
        twoFactorOtpRepository.delete(twoFactorOtp);
    }
}
