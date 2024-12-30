package com.example.springserver.api.Mypage.dto;

public record MypageReqeustDto (
        // 수정 가능한 내용들
        String username,
        String address,
        String phoneNumber,
        String parentPhoneNumber
){
}
