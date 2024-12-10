package com.market.trading.model;

import com.market.trading.domain.VerificationType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Embeddable
public class TwoFactorAuth {
    private boolean isEnabled;
    @Enumerated(EnumType.STRING)
    private VerificationType sendTo;

    public TwoFactorAuth() {
    }
}
