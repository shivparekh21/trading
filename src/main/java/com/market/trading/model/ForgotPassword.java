package com.market.trading.model;

import com.market.trading.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String resetToken;
    private LocalDateTime tokenExpiryTime;
    private String userEmail;
    private String password;
}
