package com.market.trading.model;

import com.market.trading.domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Wallet wallet;

    private WalletTransactionType transactionType;
    private LocalDate date;
    private String transferId; // transferring from one wallet to another
    private String purpose;
    private BigDecimal amount;
}
