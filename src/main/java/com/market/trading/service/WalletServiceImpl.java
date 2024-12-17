package com.market.trading.service;

import com.market.trading.domain.OrderType;
import com.market.trading.model.Order;
import com.market.trading.model.User;
import com.market.trading.model.Wallet;
import com.market.trading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getWallet(User user) {
        return walletRepository.findByUserId(user.getId());
    }

    @Override
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    @Override
    public Wallet addAmount(Wallet wallet, BigDecimal amount) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal addBalance = balance.add(amount);
        wallet.setBalance(addBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet transferToAnotherWallet(User sender, User receiver, BigDecimal amount) {
        Wallet senderWallet = getWallet(sender);

        if(senderWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Sender does not have enough money");
        }
        BigDecimal senderBalance = senderWallet.getBalance().subtract(amount);
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        Wallet receiverWallet = getWallet(receiver);
        BigDecimal receiverBalance = receiverWallet.getBalance().add(amount);
        receiverWallet.setBalance(receiverBalance);
        walletRepository.save(receiverWallet);
        return senderWallet;
    }

    @Override
    public Wallet payOrder(Order order, User user) throws Exception {
        Wallet wallet = getWallet(user);

        if(order.getOrderType().equals(OrderType.BUY)){
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            if (newBalance.compareTo(order.getPrice()) < 0) {
                throw new Exception("Order does not have enough money");
            }
            wallet.setBalance(newBalance);
        }
        else if(order.getOrderType().equals(OrderType.SELL)){
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletRepository.save(wallet);
        return wallet;
    }
}
