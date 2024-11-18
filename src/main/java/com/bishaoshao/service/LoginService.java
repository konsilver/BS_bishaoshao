package com.bishaoshao.service;


import java.util.List;
import java.util.Vector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.bishaoshao.model.CaptchaResult;
import com.bishaoshao.utils.enums.CaptchaTypeEnum;
import com.bishaoshao.config.property.CaptchaProperties;
import com.bishaoshao.utils.enums.SecurityConstants;


import java.awt.*;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class LoginService{
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private  CodeGenerator codeGenerator;
    @Autowired
    private  Font captchaFont;

    @Autowired
    private  CaptchaProperties captchaProperties;


        /**
     * 获取验证码
     *
     * @return 验证码
     */
    public CaptchaResult getCaptcha() {

        String captchaType = captchaProperties.getType();
        int width = captchaProperties.getWidth();
        int height = captchaProperties.getHeight();
        int interfereCount = captchaProperties.getInterfereCount();
        int codeLength = captchaProperties.getCode().getLength();

        AbstractCaptcha captcha;
        if (CaptchaTypeEnum.CIRCLE.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createCircleCaptcha(width, height, codeLength, interfereCount);
        } else if (CaptchaTypeEnum.GIF.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createGifCaptcha(width, height, codeLength);
        } else if (CaptchaTypeEnum.LINE.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createLineCaptcha(width, height, codeLength, interfereCount);
        } else if (CaptchaTypeEnum.SHEAR.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createShearCaptcha(width, height, codeLength, interfereCount);
        } else {
            throw new IllegalArgumentException("Invalid captcha type: " + captchaType);
        }
        captcha.setGenerator(codeGenerator);
        captcha.setTextAlpha(captchaProperties.getTextAlpha());
        captcha.setFont(captchaFont);

        String captchaCode = captcha.getCode();
        String imageBase64Data = captcha.getImageBase64Data();

        /**  验证码文本缓存至Redis，用于登录校验
        String captchaKey = IdUtil.fastSimpleUUID();
        redisTemplate.opsForValue().set(SecurityConstants.CAPTCHA_CODE_PREFIX + captchaKey, captchaCode,
                captchaProperties.getExpireSeconds(), TimeUnit.SECONDS);
        */
        return CaptchaResult.builder()
                .captchaKey( String.valueOf(solveCaptcha(captchaCode)))
                .captchaBase64(imageBase64Data)
                .build();
    }

    public static int solveCaptcha(String captchaCode) {
        if (captchaCode.endsWith("=")) {
            captchaCode = captchaCode.substring(0, captchaCode.length() - 1); // 去掉 '='
        }

        // 定义正则匹配简单数学公式（如：1+2、3*4 等）
        Pattern pattern = Pattern.compile("(\\d+)([+\\-*/])(\\d+)");
        Matcher matcher = pattern.matcher(captchaCode);

        if (matcher.matches()) {
            // 提取数字和运算符
            int operand1 = Integer.parseInt(matcher.group(1)); // 第一个数字
            String operator = matcher.group(2);               // 运算符
            int operand2 = Integer.parseInt(matcher.group(3)); // 第二个数字

            // 根据运算符计算结果
            return calculate(operand1, operator, operand2);
        } else {
            throw new IllegalArgumentException("Invalid captcha code: " + captchaCode);
        }
    }

    private static int calculate(int operand1, String operator, int operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }



}