package com.example.springserver.global.security.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailHandler {
    private JavaMailSender mailSender;
    private MimeMessage mimeMessage;
    private MimeMessageHelper mimeMessageHelper;

    // 생성자(JavaMailSender 변수)
    public MailHandler(JavaMailSender sender) throws MessagingException {
        this.mailSender = sender;
        mimeMessage = sender.createMimeMessage();
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    }

    // 송신자 설정
    public void setFrom(String fromAddress) throws MessagingException {
        mimeMessageHelper.setFrom(fromAddress);
    }

    // 수신자 설정
    public void setTo(String email) throws MessagingException {
        mimeMessageHelper.setTo(email);
    }

    // 메일 제목 설정하기
    public void setSubject(String subject) throws MessagingException {
        mimeMessageHelper.setSubject(subject);
    }

    // 메일 내용
    public void setText(String text, boolean useHtml) throws MessagingException {
        mimeMessageHelper.setText(text, useHtml);
    }

    // 발송
    public void send() {
        try {
            mailSender.send(mimeMessage);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
