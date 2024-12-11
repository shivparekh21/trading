package com.market.trading.service;

import com.market.trading.config.JwtProvider;
import com.market.trading.domain.VerificationType;
import com.market.trading.model.TwoFactorAuth;
import com.market.trading.model.User;
import com.market.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not Found");
        }
        return user;
    }

    @Override
    public User findUserById(long id) throws Exception {
        //userRepository.findById(id) returns an Optional<User>.
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new Exception("User not Found");
        }
        return user.get();
    }

    @Override
    public void enableTwoFactorAuthentication(VerificationType verificationType, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setVerificationType(verificationType);

        user.setTwoFactorAuth(twoFactorAuth);
        User savedUser = userRepository.save(user);
        System.out.println("2FA Enabled for: " + savedUser.getEmail() +
                " | isEnabled: " + savedUser.getTwoFactorAuth().isEnabled());
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
