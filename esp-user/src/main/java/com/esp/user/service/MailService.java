package com.esp.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ProjectName: user-service
 * @Auther: GERRY
 * @Date: 2018/11/13 17:45
 * @Description:
 */
@Service
public class MailService {
    @Resource
    private MailSender mailSender;

    @Value("${spring.mail.username}") // 自己的邮箱
    private String sender;

    public void sendMail(String subject, String content, String toMail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(subject); // 设置主题
        mailMessage.setText(content); // 设置内容
        mailMessage.setFrom(sender); // 设置发送人
        mailMessage.setTo(toMail); // 设置接受人
        mailSender.send(mailMessage); // 发送邮件
    }
}
