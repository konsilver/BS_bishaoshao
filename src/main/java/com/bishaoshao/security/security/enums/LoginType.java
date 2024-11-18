package com.bishaoshao.security.security.enums;

import lombok.Getter;


//  后续若有更多角色，通过这个添加
public enum LoginType {
    USER("USER");

    @Getter
    private final String role; 

    LoginType(String role) {
        this.role = role; 
    }
    
}
