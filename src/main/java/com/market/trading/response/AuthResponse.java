package com.market.trading.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwtToken;
    private boolean status;
    private String message;
    private boolean isTwoAuthEnabled;
    private String session;

}
