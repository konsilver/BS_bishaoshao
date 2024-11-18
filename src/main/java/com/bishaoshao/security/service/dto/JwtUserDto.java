package com.bishaoshao.security.service.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bishaoshao.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserDto implements UserDetails {
    private String username;

    private String password; 

    private Collection<AuthorityDto> authorities;

    private boolean enabled = true;

    public JwtUserDto(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.emptyList(); // 提供默认的空权限
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return true; 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; 
    }


}
