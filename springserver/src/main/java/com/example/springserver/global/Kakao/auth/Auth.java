package com.example.springserver.global.Kakao.auth;

import com.example.springserver.api.MyPage.Domain.Member;
import lombok.Data;
import lombok.Getter;

@Getter
public class Auth {

    @Data
    public static class SignIn {
        private String userId;
        private String password;
    }

    @Data
    public static class SignUp {
        private String userId;
        private String password;
        //private List<String> roles;

        public Member toEntity() {
            return Member.builder()
                    .userId(this.userId)
                    .password(this.password)
                    .build();
        }
    }
}
