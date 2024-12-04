package com.bishaoshao.service;



import javax.management.Query;
import org.mybatis.spring.annotation.MapperScan;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.bishaoshao.entity.User;
import com.bishaoshao.mapper.InfoMapper;
import com.bishaoshao.utils.result.Result;
import com.bishaoshao.utils.result.ResultCode;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class InfoService {
    @Autowired
    private InfoMapper InfoMapper;     

    public Result<Void> registerNewUser(User user) {
        try {
            InfoMapper.insert(user);
            return Result.success(); 
        } catch(Exception e) {
            return Result.failed("用户已存在");
        }
    }

    public Result<Void> updatePasswordByEmail(String email, String newPassword) {
        try {
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            wrapper.eq("email", email);
            wrapper.set("password", newPassword);
            InfoMapper.update(wrapper);
            return Result.success(); 
        } catch(Exception e) {
            return Result.failed();
        }
    }

    public Result<User> checkPassword(String username, String password) {
        try {
            User res = InfoMapper.selectOne(new QueryWrapper<User>().eq("username", username));
            if (res == null) 
                return Result.failed(ResultCode.USER_NOT_EXIST);
            if (password.equals(res.getPassword()))
                return Result.success(res);
            else 
                return Result.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        } catch (Exception e) {
            return Result.failed();
        }
    }

    public Result<User> getUserById(Long id) {
        try {
            User res = InfoMapper.selectById(id);
            if (res == null)
                return Result.failed(ResultCode.USER_NOT_EXIST);
            return Result.success(res);
        } catch(Exception e) {
            return Result.failed();
        }
    }

    public Result<User> getUserByEmail(String email) {
        try {
            User res = InfoMapper.selectOne(new QueryWrapper<User>().eq("email", email));
            if (Objects.isNull(res))
            return Result.failed(ResultCode.EMAIL_NOT_EXIST);
            return Result.success(res);
        } catch(Exception e) {
            return Result.failed();
        }
    }

    public Result<User> getUserByUsername(String username) {

        try {
            User res = InfoMapper.selectOne(new QueryWrapper<User>().eq("username", username));
            if (Objects.isNull(res))
                 return Result.failed(ResultCode.USERNAME_NOT_EXIST);
                 return Result.success(res);
            } catch(Exception e) {
                return Result.failed();
            }
    }

    public Result<Void> updateUserByUsername(String username, String phoneNumber, String new_password) {
        try {
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            wrapper.eq("username", username);
            wrapper.set("phone_number", phoneNumber);
            wrapper.set("password", new_password);
            InfoMapper.update(wrapper);
            return Result.success();
        } catch(Exception e) {
            return Result.failed();
        }
    }
}
