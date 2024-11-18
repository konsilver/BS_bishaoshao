package com.bishaoshao.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bishaoshao.security.config.UserCacheConfig;
import com.bishaoshao.security.service.dto.JwtUserDto;
import com.bishaoshao.utils.RedisUtils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserCacheManager {
    @Autowired
    private RedisUtils redisUtils; 
    @Autowired
    private UserCacheConfig userCacheConfig;

    /*
     * 获取用户缓存的数据
     */
    public JwtUserDto getUserCache(String username) {
        if (username != "") {
            Object obj = redisUtils.get(cacheKey(username));
            if (obj != null)
                return (JwtUserDto) obj; 
        }
        return null;
    }

    /*
     * 添加用户数据到缓存
     */
    public void addUserCache(String username, JwtUserDto jwtUserDto) {
        if (username != null) {
            long time = userCacheConfig.getValidityTimeInSeconds() + RandomUtil.randomInt(999, 1999);
            redisUtils.set(cacheKey(username), jwtUserDto, time);
        }
    } 

    public void cleanUserCache(String username) {
        if (!StrUtil.isBlank(username)) {
            log.info(username); 
            redisUtils.del(cacheKey(username));
        }
    }

    public String cacheKey(String username) {
        return userCacheConfig.getUserCacheKey() + username;
    }
}
