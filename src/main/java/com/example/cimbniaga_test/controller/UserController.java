package com.example.cimbniaga_test.controller;

import com.example.cimbniaga_test.model.UserAuth;
import com.example.cimbniaga_test.service.UserService;
import org.hibernate.mapping.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserAuth> createUser(@RequestBody UserAuth request){
        UserAuth createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //signing in
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserAuth request){
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            String token = userService.signIn(username, password);
//            HttpHeaders responseHeaders = new HttpHeaders();
//            responseHeaders.set("Authorization", "Bearer " + token);
//            return ResponseEntity.ok().headers(responseHeaders).build();
            Map<String, String> response = new HashMap<>();
            response.put("token", "Bearer " + token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
