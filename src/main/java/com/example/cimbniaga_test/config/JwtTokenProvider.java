package com.example.cimbniaga_test.config;

import com.example.cimbniaga_test.model.UserAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

import com.example.cimbniaga_test.repository.UserAuthRepository;
import com.example.cimbniaga_test.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;
    @Autowired
    private AuthenticationManager authenticationManager;


    public String generateToken(UserAuth userAuth) {
        Date now = new Date();
        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(userAuth.getUsername(), userAuth.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                authenticate
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        String issuer = userAuth.getId().toString();


        byte[] secretKeyBytes = Encoders.BASE64.encode(jwtSecret.getBytes()).getBytes();

        Key key = Keys.hmacShaKeyFor(secretKeyBytes);

        String jwt = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuer(issuer)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
        return jwt;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, SecretKey key) {
        try {

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;

        }

    }

}
