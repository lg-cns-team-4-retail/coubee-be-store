package com.coubee.coubeebestore.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRegisterDto {
    private String storeName;       // 매장이름
    private String description;     // 매장 설명
    private String contactNo;       // 연락처
    private String storeAddress;     // 주소
    private Double latitude;        // 위도
    private Double longitude;       // 경도
    private String bizNo;           // 사업자 등록번호
    private String backImg;          // 배경 이미지 URL
    private String profileImg;      // 프로필 이미지 URL
    private String category;  // 카테고리
}