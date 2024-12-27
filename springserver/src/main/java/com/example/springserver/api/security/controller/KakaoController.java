package com.example.springserver.api.security.controller;
import org.springframework.ui.Model;

import com.example.springserver.api.security.OpenFeign.dto.respond.UserAuthResponseDto;
import com.example.springserver.api.security.OpenFeign.dto.respond.UserReissueRequestDto;
import com.example.springserver.api.security.OpenFeign.dto.respond.UserSignUpRequestDto;
import com.example.springserver.api.security.OpenFeign.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class KakaoController {
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<UserAuthResponseDto> signIn(@RequestParam String token) {
        UserAuthResponseDto response = authService.signIn(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserAuthResponseDto> signUp(@RequestParam String token, @RequestBody UserSignUpRequestDto request) {
        UserAuthResponseDto response = authService.signUp(token, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<Token> reissue(@RequestParam String refreshToken, @RequestBody UserReissueRequestDto request) {
        Token token = authService.reissue(refreshToken, request);
        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/signout/{userId}")
    public ResponseEntity<Void> signOut(@PathVariable Long userId) {
        authService.signOut(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/withdraw/{userId}")
    public ResponseEntity<Void> withdraw(@PathVariable Long userId) {
        authService.withdraw(userId);
        return ResponseEntity.ok().build();
    }
    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        model.addAttribute("location", location);

        return "login";
    }
}