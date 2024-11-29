package com.example.springserver.api.security.domain;

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

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .id(this.userId)
                    .password(this.password)
                    .build();
        }
    }
}
