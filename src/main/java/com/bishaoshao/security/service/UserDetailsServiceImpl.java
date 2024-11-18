package com.bishaoshao.security.service;

import java.util.Collections;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import com.bishaoshao.entity.User;
import com.bishaoshao.security.security.enums.LoginType;
import com.bishaoshao.security.service.dto.AuthorityDto;
import com.bishaoshao.security.service.dto.JwtUserDto;
import com.bishaoshao.service.InfoService;
import com.bishaoshao.utils.result.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserCacheManager userCacheManager; 
    @Autowired
    private InfoService InfoService; 

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        

        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if (Objects.isNull(jwtUserDto)) {
            try {
                LoginType loginType = LoginType.valueOf(username.split("-")[0]); 
                switch (loginType) {
                    case USER:
                        User user = (User) InfoService.getUserByUsername(username.split("-")[1]).getData();
                        if (user == null) {
                            return null;
                        } else {
                            jwtUserDto = new JwtUserDto(
                                user.getUsername(),
                                user.getPassword()
                            );
                        }
                        break;
                    default:
                        return null; 
                }
                
            } catch(Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
            userCacheManager.addUserCache(username, jwtUserDto);
        }
        return jwtUserDto;
    }
}
