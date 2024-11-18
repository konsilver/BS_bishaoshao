package com.bishaoshao.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bishaoshao.entity.User;
import com.bishaoshao.service.InfoService;
import com.bishaoshao.utils.result.Result;
import com.bishaoshao.utils.result.ResultCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrentInfoService {
    @Autowired
    private InfoService InfoService; 

    public Result<User> getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Result.failed(ResultCode.USER_LOGIN_OVER);
        }
        String username = (String) authentication.getPrincipal(); 
        return InfoService.getUserByUsername(username.split("-")[1]); 
    }

    public Result<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Result.failed(ResultCode.USER_LOGIN_OVER);
        }
        String username = (String) authentication.getPrincipal(); 
        return Result.success(username.split("-")[1]);
    }


    public Result<Long> getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Result.failed(ResultCode.USER_LOGIN_OVER);
        }
        String username = (String) authentication.getPrincipal(); 
        User res = InfoService.getUserByUsername(username.split("-")[1]).getData();
        if (res==null) {
            return Result.failed(ResultCode.USER_LOGIN_ERROR);
        }
        return Result.success(res.getId()); 
    }
}
 
