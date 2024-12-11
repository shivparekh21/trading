package com.market.trading.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-phone-number}")
    private String fromPhoneNumber;

    //Why It Doesn’t Work on Constructors
    //When a class is created, Spring Boot runs the constructor first.
    //However, the properties annotated with @Value or @Autowired are not yet set during constructor execution.
    //@PostConstruct runs after Spring sets all properties, ensuring they are initialized properly.
    //@PostConstruct don't apply on constructor
    @PostConstruct
    public void SmsService() {
        Twilio.init(accountSid, authToken);  // Initialize Twilio
    }

    public void sendVerificationOtpSms(String phoneNumber, String otp) {
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumber),  // To
                        new com.twilio.type.PhoneNumber(fromPhoneNumber),  // From
                        "Your OTP is: " + otp)  // Message
                .create();

        System.out.println("SMS sent successfully to " + phoneNumber);
    }
}
