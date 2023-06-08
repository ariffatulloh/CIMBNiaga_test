package com.example.cimbniaga_test.service;

import com.example.cimbniaga_test.config.JwtTokenProvider;
import com.example.cimbniaga_test.exception.UserAlreadyExistsException;
import com.example.cimbniaga_test.model.UserAuth;
import com.example.cimbniaga_test.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {



    private final UserAuthRepository userAuthRepository;

    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService( UserAuthRepository userAuthRepository,JwtTokenProvider jwtTokenProvider){
        this.userAuthRepository= userAuthRepository;
        this.jwtTokenProvider=jwtTokenProvider;

    }

    public UserAuth createUser(UserAuth userEntity) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserAuth result =userAuthRepository.save(userEntity);
        return result;
    }
    public String signIn(String username, String password) throws AuthenticationException {
        UserAuth user = userAuthRepository.findByUsername(username);
        if(user == null){
            throw new UserAlreadyExistsException("Invalid username or password.");
        }


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password.") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }

        // Generate and return the JWT token
        UserAuth userGenerateToken = new UserAuth();
        userGenerateToken.setId(user.getId());
        userGenerateToken.setUsername(username);
        userGenerateToken.setPassword(password);
        return jwtTokenProvider.generateToken(userGenerateToken);
    }
}
