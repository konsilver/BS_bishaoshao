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
import com.bishaoshao.model.SearchResult;
import com.bishaoshao.utils.result.Result;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


@Slf4j
@Service
public class SearchService {

    public Result<List<SearchResult>> search(String keyword){

        List<SearchResult> results = List.of(
            new SearchResult(1L, "商品1", "2024-11-19", 100.0, "淘宝"),
            new SearchResult(2L, "商品2", "2024-11-18", 200.0, "京东")
        );

        return Result.success(results);

    }


    
}
