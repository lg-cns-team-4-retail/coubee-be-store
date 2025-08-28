package com.coubee.coubeebestore.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreResponseDto {
    private Long storeId;
    private String storeName;       // 매장이름
    private String description;     // 매장 설명
    private String contactNo;       // 연락처
    private String storeAddress;     // 주소
    private String workingHour;
    private String backImg;          // 배경 이미지 URL
    private String profileImg;      // 프로필 이미지 URL
    private double longitude;
    private double latitude;
//    private String storeTag;  // 카테고리
    private List<CategoryDto> storeTag;
    private double distance;
    private HotdealResponseDto hotdeal; // 핫딜
    private boolean interest; // 관심 매장 여부
    private boolean fallback = false;
}
