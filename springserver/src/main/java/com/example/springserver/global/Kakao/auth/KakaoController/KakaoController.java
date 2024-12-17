package com.example.springserver.global.Kakao.auth.KakaoController;

import com.example.springserver.global.Kakao.auth.Dto.respond.Token;
import com.example.springserver.global.Kakao.auth.Dto.respond.UserAuthResponseDto;
import com.example.springserver.global.Kakao.auth.Dto.respond.UserReissueRequestDto;
import com.example.springserver.global.Kakao.auth.Dto.respond.UserSignUpRequestDto;
import com.example.springserver.global.Kakao.auth.OpenFeign.AuthService;
import lombok.RequiredArgsConstructor;
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
}