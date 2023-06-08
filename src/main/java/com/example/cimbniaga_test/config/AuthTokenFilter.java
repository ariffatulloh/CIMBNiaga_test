package com.example.cimbniaga_test.config;

import com.example.cimbniaga_test.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class AuthTokenFilter extends OncePerRequestFilter {

    @Value("${app.jwtSecret}")
    private String jwtSecret;


    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        try {
            String jwt = parseJwt(request);
            SecretKey key = Keys.hmacShaKeyFor(Encoders.BASE64.encode(jwtSecret.getBytes()).getBytes());
            if (jwt != null && jwtTokenProvider.validateToken(jwt, key)) {
                String email = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {
            logger.warn("Request to parse expired JWT failed: {}"+ exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            logger.warn("Request to parse unsupported JWT failed: {}"+ exception.getMessage());
        } catch (MalformedJwtException exception) {
            logger.warn("Request to parse invalid JWT failed: {}"+ exception.getMessage());
        } catch (SignatureException exception) {
            logger.warn("Request to parse JWT with invalid signature failed: {}"+ exception.getMessage());
        } catch (IllegalArgumentException exception) {
            logger.warn("Request to parse empty or null JWT failed: {}"+ exception.getMessage());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
