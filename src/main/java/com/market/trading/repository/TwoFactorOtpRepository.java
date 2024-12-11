package com.market.trading.repository;

import com.market.trading.model.TwoFactorOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOtp, String> {
    TwoFactorOtp findByUserId(Long userId);
}
