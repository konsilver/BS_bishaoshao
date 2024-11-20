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
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.time.LocalDate;


@Slf4j
@Service
public class SearchService {

    public Result<List<SearchResult>> search(String keyword){

        List<SearchResult> results = List.of(
            new SearchResult(1, "商品1", LocalDate.of(2024, 11, 19), 100.0, "淘宝", 
                "https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg"),
            new SearchResult(2, "商品2", LocalDate.parse("2024-11-18"), 200.0, "京东", 
                "https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg"),
            new SearchResult(3, "商品3", LocalDate.parse("2024-11-18"), 200.0, "淘宝", 
                "https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg"),
            new SearchResult(4, "商品4", LocalDate.parse("2024-11-18"), 200.0, "京东", 
                "https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg"),
            new SearchResult(5, "商品5", LocalDate.parse("2024-11-18"), 200.0, "淘宝", 
                "https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg"),
            new SearchResult(6, "商品6", LocalDate.parse("2024-11-18"), 200.0, "京东", 
                "https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg"),
            new SearchResult(7, "商品7", LocalDate.parse("2024-11-18"), 200.0, "淘宝", 
                "https://img14.360buyimg.com/pop/jfs/t1/180221/38/52140/148950/6738afa6F567ae07f/371869cfa5f0c449.jpg")
        );
        return Result.success(results);

    }


    
}
