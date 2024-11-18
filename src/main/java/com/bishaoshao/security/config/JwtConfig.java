package com.bishaoshao.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String header;
    private String tokenStartWith; 
    private Long tokenValidityInSeconds; 
    private String tokenSignKey; 
    private String onlineKey; 
    private Long detectTime;
    private Long renewTime; 
}
