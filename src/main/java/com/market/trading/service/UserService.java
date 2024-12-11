package com.market.trading.service;

import com.market.trading.domain.VerificationType;
import com.market.trading.model.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public User findUserById(long id) throws Exception;
    public void enableTwoFactorAuthentication(VerificationType verificationType, User user);
    public User updatePassword(User user, String newPassword);
}
