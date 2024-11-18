package com.bishaoshao.security.controller.dto;

import lombok.Data;


@Data
public class UserForgetReq {
    private String password; 
    private String mail;
}
