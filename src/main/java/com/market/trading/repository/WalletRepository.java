package com.market.trading.repository;

import com.market.trading.model.User;
import com.market.trading.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long user_id);
}
