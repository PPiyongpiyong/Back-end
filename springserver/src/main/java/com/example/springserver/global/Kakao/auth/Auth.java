package com.example.springserver.global.Kakao.auth;

import com.example.springserver.api.MyPage.Domain.MemberEntity;
import lombok.Data;
import lombok.Getter;

import java.util.List;

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

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .userId(this.userId)
                    .password(this.password)
                    .build();
        }
    }
}
