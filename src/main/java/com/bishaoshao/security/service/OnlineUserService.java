package com.bishaoshao.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bishaoshao.security.config.JwtConfig;
import com.bishaoshao.security.security.JwtTokenProvider;
import com.bishaoshao.security.service.dto.JwtUserDto;
import com.bishaoshao.security.service.dto.OnlineUserDto;
import com.bishaoshao.utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OnlineUserService {
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired
    private RedisUtils redisUtils; 
    @Autowired 
    private JwtTokenProvider jwtTokenProvider; 


    /* 
     * 保存在线用户信息
     */
    public void save(JwtUserDto jwtUserDto, String token) {
        OnlineUserDto onlineUserDto = null;
        try {
            onlineUserDto = new OnlineUserDto(jwtUserDto.getUsername(), token, new Date()); 
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            return ;
        }
        String loginKey = jwtTokenProvider.loginKey(token);
        log.info(loginKey);
        redisUtils.set(loginKey, onlineUserDto, jwtConfig.getTokenValidityInSeconds());
    }

    /*
     * 在线用户登出
     */
    public void logout(String token) {
        String loginKey = jwtTokenProvider.loginKey(token);
        redisUtils.del(loginKey);
    }

    /*
     * 获得在线用户
     */
    public OnlineUserDto getOnlineUser(String token) {
        String loginKey = jwtTokenProvider.loginKey(token);
        return (OnlineUserDto) redisUtils.get(loginKey);
    }
}
