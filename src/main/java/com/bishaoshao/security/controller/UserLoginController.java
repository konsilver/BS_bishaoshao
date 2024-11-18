package com.bishaoshao.security.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bishaoshao.entity.User;
import com.bishaoshao.security.config.JwtConfig;
import com.bishaoshao.security.controller.dto.SendMailReq;
import com.bishaoshao.security.controller.dto.UserLoginReq;
import com.bishaoshao.security.controller.dto.UserRegisterReq;
import com.bishaoshao.security.controller.dto.UserForgetReq;
import com.bishaoshao.security.security.JwtTokenProvider;
import com.bishaoshao.security.security.enums.LoginType;
import com.bishaoshao.security.service.CurrentInfoService;
import com.bishaoshao.security.service.OnlineUserService;
import com.bishaoshao.security.service.UserCacheManager;
import com.bishaoshao.security.service.dto.JwtUserDto;
import com.bishaoshao.service.InfoService;
import com.bishaoshao.service.MailService;
import com.bishaoshao.utils.result.*;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/login")
public class UserLoginController {
    @Autowired
    private AuthenticationManager authenticationManager; 
    @Autowired
    private JwtTokenProvider jwtTokenProvider; 
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired
    private OnlineUserService onlineUserService; 
    @Autowired
    private InfoService InfoService; 
    @Autowired
    private MailService MailService;
    @Autowired
    private CurrentInfoService currentInfoService; 
    @Autowired
    private UserCacheManager userCacheManager; 

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    
    @PostMapping("/in")
    public Result<?> postUserLogin(@Validated @RequestBody UserLoginReq req) {
        String username = req.getUsername();
        String password = req.getPassword();

        //若为邮箱登录则替换为用户名
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(username);
        if(matcher.matches()){
            User user = InfoService.getUserByEmail(username).data;
            if (user == null) {
                return Result.failed("邮箱对应的用户不存在");
            }
            username = user.getUsername();
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
            new UsernamePasswordAuthenticationToken(LoginType.USER + "-" + username, password);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch(AuthenticationException e) {
            log.info(e.getMessage(), e);
            return Result.failed("账号名或密码错误");
        }
        String token = jwtTokenProvider.createToken(authentication, LoginType.USER);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", jwtConfig.getTokenStartWith() + " " + token);
            put("username", jwtUserDto.getUsername());
        }}; 
        onlineUserService.save(jwtUserDto, token);

        return Result.success(authInfo);
    }

    @PostMapping("/mail")
    public Result<String> postSendMail(@RequestBody SendMailReq req) {
        String code=MailService.sendVerificationCode(req.getMail());
        return Result.success(code);
    }

    @PostMapping("/register")
    public Result<?> postUserRegister(@RequestBody UserRegisterReq req) {
        User user = new User(null, req.getUsername(), req.getPassword(),  req.getMail());
        Result<?> result_over = InfoService.registerNewUser(user);

        if(!Result.isSuccess(result_over)) return result_over;

        return Result.success();
    }

    @PostMapping("/forget")
    public Result<?> postUserForget(@RequestBody UserForgetReq req) {
        
        Result<?> result_over = InfoService.updatePasswordByEmail(req.getMail(),req.getPassword());

        if(!Result.isSuccess(result_over)) return result_over;

        return Result.success();
    }
}
