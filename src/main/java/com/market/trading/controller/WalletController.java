package com.market.trading.controller;

import com.market.trading.model.Order;
import com.market.trading.model.User;
import com.market.trading.model.Wallet;
import com.market.trading.model.WalletTransaction;
import com.market.trading.repository.UserRepository;
import com.market.trading.service.TwoFactorOtpService;
import com.market.trading.service.UserService;
import com.market.trading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwtToken) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        Wallet wallet = walletService.getWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/wallet/transfer")
    public ResponseEntity<Wallet> transferToAnotherWallet(@RequestHeader("Authorization") String jwtToken,
                                                          @RequestParam Long receiverId,
                                                          @RequestBody WalletTransaction walletTransaction) throws Exception {
        User sender = userService.findUserProfileByJwt(jwtToken);
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
        Wallet wallet = walletService.transferToAnotherWallet(sender, receiver, walletTransaction.getAmount());
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/wallet/order-payment")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String jwtToken,
                                                          @RequestParam Long orderId) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrder(order, user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

}
