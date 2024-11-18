package com.bishaoshao.security.security;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("---------Start Jwt token authenticate---------");
        String username = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        log.info("login info: {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (Objects.isNull(userDetails)) {
            throw new AuthenticationCredentialsNotFoundException("username not found");
        }
        log.info("password: {}, current password: {}", password, userDetails.getPassword()); 
        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Password error"); 
        }
        

        log.info("user {} authenticated successfully", username);
        log.info("---------End Jwt token authenticate---------");
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}