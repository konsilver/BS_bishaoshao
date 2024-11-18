package com.bishaoshao.security.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserRegisterReq {
    private String username;
    private String password;
    private String mail;
}
