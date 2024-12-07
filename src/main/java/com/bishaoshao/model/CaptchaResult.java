package com.bishaoshao.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码响应对象
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResult {


    private String captchaKey;


    private String captchaBase64;

}
