package com.bishaoshao.controller;

import com.bishaoshao.security.config.JwtConfig;
import com.bishaoshao.security.security.JwtTokenProvider;
import com.bishaoshao.security.service.CurrentInfoService;
import com.bishaoshao.security.service.OnlineUserService;
import com.bishaoshao.service.SubscribeService;
import com.bishaoshao.utils.result.Result;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/subscribe")
public class SubscribeController {


    @Autowired
    private AuthenticationManager authenticationManager; 
    @Autowired
    private JwtTokenProvider jwtTokenProvider; 
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired
    private OnlineUserService onlineUserService; 
    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    private CurrentInfoService currentInfoService; 



    @PostMapping("/add")
    public Result<?> add(@RequestBody Map<String, String> params) {
                
        Long user_id=currentInfoService.getCurrentUserId().getData();
        long thing_id = Long.parseLong(params.get("thing_id"));
        return subscribeService.add(user_id,thing_id);

    }

    

    @PostMapping("/sub")
    public Result<?> sub(@RequestBody Map<String, String> params) {

        Long user_id=currentInfoService.getCurrentUserId().getData();
        long thing_id = Long.parseLong(params.get("thing_id"));
        return subscribeService.sub(user_id,thing_id);
    }


    @GetMapping("/all")
    public Result<?> getall(){
        return subscribeService.recmall();
    }
    

    @GetMapping("/like")
    public Result<?> getlike(){
        Long user_id=currentInfoService.getCurrentUserId().getData();
        return subscribeService.recmlike(user_id);
    }

    @PostMapping("/issub")
    public Result<?> getissub(@RequestBody Map<String, String> params){
        Long user_id=currentInfoService.getCurrentUserId().getData();
        long thing_id = Long.parseLong(params.get("id"));
        return subscribeService.sure(user_id,thing_id);
    }

    @GetMapping("/remind")
    public Result<?> remind(){
        return subscribeService.remind();
    }
}
