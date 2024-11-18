package com.bishaoshao.security.service.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
 * 当前在线用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserDto implements Serializable {
    private String username; 
    private String key; 
    private Date loginTime; 
}
