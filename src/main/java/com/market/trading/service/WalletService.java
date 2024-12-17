package com.market.trading.service;

import com.market.trading.model.Order;
import com.market.trading.model.User;
import com.market.trading.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet getWallet(User user); //User Wallet
    Wallet getWalletByUserId(Long userId); // User Wallet by Id
    Wallet addAmount(Wallet wallet, BigDecimal amount);
    Wallet transferToAnotherWallet(User sender, User receiver, BigDecimal amount);
    Wallet payOrder(Order order, User user) throws Exception;

}
