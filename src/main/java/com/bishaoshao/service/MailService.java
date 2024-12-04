package com.bishaoshao.service;

import com.bishaoshao.config.property.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import org.springframework.stereotype.Service;
/**
 * 邮件服务接口层
 *
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    /**
     * 发送邮件并返回验证码
     */
    public String sendVerificationCode(String email) {
        // 生成验证码
        String code = generateVerificationCode();

        // 发送邮件
        sendMail(email, "邮箱验证码", "您注册比少少账号的验证码是: " + code);

        return code; // 返回验证码
    }

    /**
     * 生成6位随机验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // 生成 6 位数字
        return String.valueOf(code);
    }

    /**
     * 发送邮件的通用方法
     */
    private void sendMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getFrom());
            //System.out.println(mailProperties.getFrom());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送失败: " + e.getMessage());
        }
    }

    public void remind(String mail,String msg){
        sendMail(mail, "降价提醒", msg);
        //System.out.println(msg);
    }
}

