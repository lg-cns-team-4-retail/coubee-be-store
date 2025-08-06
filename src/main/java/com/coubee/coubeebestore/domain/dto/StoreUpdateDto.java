package com.coubee.coubeebestore.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreUpdateDto {
    private Long storeId;
    private String description;     // 매장 설명
    private String contactNo;       // 연락처
    private String workingHour;
    private String backImg;          // 배경 이미지 URL
    private String profileImg;      // 프로필 이미지 URL
    private String storeTag;  // 카테고리
}
