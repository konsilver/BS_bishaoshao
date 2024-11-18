package com.bishaoshao.controller;


import com.bishaoshao.utils.result.Result;
import com.bishaoshao.model.CaptchaResult;
import com.bishaoshao.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/login")
public class LoginController{

    @Autowired
    private LoginService LoginService;

    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public Result<CaptchaResult> getCaptcha() {
        CaptchaResult captcha = LoginService.getCaptcha();
        return Result.success(captcha);
    }
}
