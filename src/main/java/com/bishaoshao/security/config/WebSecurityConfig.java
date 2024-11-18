package com.bishaoshao.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bishaoshao.security.security.JwtAuthenticationProvider;
import com.bishaoshao.security.security.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(CsrfConfigurer::disable)
            .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizationRegistry -> authorizationRegistry
                .requestMatchers(HttpMethod.GET, "/", "/*.html").permitAll()
                //登录，注册，图片验证码，邮箱验证码
                .requestMatchers("/api/login/*").permitAll()
                .requestMatchers("/fc/data_operator/start/login", "/fc/data_opertaor/start/register").permitAll()
                // 跨域的一次 OPTION 预检
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
            )
            .headers(headersConfigurer -> headersConfigurer
                .cacheControl(HeadersConfigurer.CacheControlConfig::disable))
            .authenticationProvider(jwtAuthenticationProvider())
            .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            ;
        return httpSecurity.build();
    }
}
