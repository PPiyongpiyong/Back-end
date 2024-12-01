package com.example.springserver.api.security.domain;

import com.example.springserver.api.security.domain.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberEntity {

    @Id
    @Column(unique = true, nullable = false, name = "member_id")
    private String id;

    private String username;

    private String password;

    // Enum 타입을 컬렉션으로 저장
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    private String phoneNumber;
    private String gender;
    private String parentPhoneNumber;
    private String address;
    private String residentNo;
}
