package com.example.springserver.global.security.service;

import com.example.springserver.global.security.dto.VerificationRequestDto;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailHandler mailHandler;

    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

    // 랜덤 인증코드 생성하기
    public String createCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            int numbers = random.nextInt(10);
            code.append(numbers);
        }
        return code.toString();
    }

    // 인증번호 전송하기
    public void mailSend(VerificationRequestDto request, String code) throws MessagingException {

        mailHandler.setFrom(FROM_ADDRESS);
        mailHandler.setTo(request.email());
        mailHandler.setSubject("[삐용삐용] " + request.email() + "님의 인증 메일입니다.");
        mailHandler.setText(
                "다음 인증코드를 입력해주세요./n" + code,
                true);
        mailHandler.send();
    }
}
