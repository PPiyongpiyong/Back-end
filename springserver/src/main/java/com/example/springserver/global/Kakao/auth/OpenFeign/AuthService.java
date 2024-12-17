package com.example.springserver.global.Kakao.auth.OpenFeign;
import com.example.springserver.global.Kakao.auth.Dto.respond.Token;
import com.example.springserver.global.Kakao.auth.Dto.respond.UserAuthResponseDto;
import com.example.springserver.global.Kakao.auth.Dto.respond.UserReissueRequestDto;
import com.example.springserver.global.Kakao.auth.Dto.respond.UserSignUpRequestDto;
import com.example.springserver.global.Kakao.auth.Error.ConflictException;
import com.example.springserver.global.Kakao.auth.Error.KaKaoUnauthorizedException;
import com.example.springserver.global.Kakao.auth.Error.ErrorStatus;
import com.example.springserver.global.Kakao.auth.KakaoRepository.UserRepository;
import com.example.springserver.global.Kakao.auth.jwt.JwtProvider;
import com.example.springserver.global.Kakao.auth.jwt.JwtValidator;
import com.example.springserver.global.Kakao.auth.Domain.User;

import jakarta.persistence.EntityNotFoundException;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
@Transactional
@Service
@Setter
@Getter
@Builder
public class AuthService {
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final KakaoOAuthProvider kakaoOAuthProvider;
    private final UserRepository userRepository;

    public UserAuthResponseDto signIn(String token) {
        String platformId = kakaoOAuthProvider.getKakaoPlatformId(token);
        User findUser = getUser(platformId);
        Token issuedToken = issueAccessTokenAndRefreshToken(findUser);
        updateRefreshToken(findUser, issuedToken.refreshToken());
        return UserAuthResponseDto.of(issuedToken, findUser);
    }

    public UserAuthResponseDto signUp(String token, UserSignUpRequestDto request) {
        validateDuplicateNickname(request.nickname());
        String platformId = kakaoOAuthProvider.getKakaoPlatformId(token);
        validateDuplicateUser(platformId);
        User user = createUser(platformId, request.nickname());
        User savedUser = userRepository.save(user);
        Token issuedToken = issueAccessTokenAndRefreshToken(savedUser);
        updateRefreshToken(savedUser, issuedToken.refreshToken());
        return UserAuthResponseDto.of(issuedToken, savedUser);
    }



    @Transactional(noRollbackFor = KaKaoUnauthorizedException.class)
    public Token reissue(String refreshToken, UserReissueRequestDto request) {
        Long userId = request.userId();
        User findUser = getUser(userId);
        validateRefreshToken(userId, refreshToken, findUser.getRefreshToken());
        Token issuedToken = issueAccessTokenAndRefreshToken(findUser);
        updateRefreshToken(findUser, issuedToken.refreshToken()); //getRefreshToken사용하는 방법을 모르겠음
        return issuedToken;
    }

    public void signOut(Long userId) {
        User findUser = getUser(userId);
        deleteRefreshToken(findUser);
    }

    public void withdraw(Long userId) {
        userRepository.deleteById(userId);
    }

    private User getUser(String platformId) {
        return userRepository.findUserByPlatformId(platformId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.USER_NOT_FOUND)));
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.existsUserByNickname(nickname)) {
            throw new ConflictException(ErrorStatus.DUPLICATE_NICKNAME);
        }
    }

    private void validateDuplicateUser(String platformId) {
        if (userRepository.existsUserByPlatformId(platformId)) {
            throw new ConflictException(ErrorStatus.DUPLICATE_USER);
        }
    }

    private void validateRefreshToken(Long userId, String refreshToken, String storedRefreshToken) {
        try {
            jwtValidator.validateRefreshToken(refreshToken);
            jwtValidator.equalsRefreshToken(refreshToken, storedRefreshToken);
        } catch (KaKaoUnauthorizedException e) {
            signOut(userId);
            throw e;
        }
    }

    private Token issueAccessTokenAndRefreshToken(User user) {
        return jwtProvider.issueToken(user.getId());
    }

    private void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(ErrorStatus.USER_NOT_FOUND)));
    }

    private void deleteRefreshToken(User findUser) {
        findUser.updateRefreshToken(null);
    }

    private User createUser(String platformId, String nickname) {
        return User.builder()
                .platformId(platformId)
                .nickname(nickname)
                .build();
    }
}