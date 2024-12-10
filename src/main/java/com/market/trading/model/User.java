package com.market.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.market.trading.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String email;

    // whenever we fetch user the password won't come
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    //default role
    private USER_ROLE role = USER_ROLE.CUSTOMER;

}

