package com.market.trading.repository;

import com.market.trading.model.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {
    Optional<ForgotPassword> findByUserEmail(String userEmail);
    Optional<ForgotPassword> findByResetToken(String resetToken);
}
