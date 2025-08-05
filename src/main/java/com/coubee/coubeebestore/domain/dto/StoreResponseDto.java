package com.coubee.coubeebestore.domain.dto;

import com.coubee.coubeebestore.domain.Category;
import com.coubee.coubeebestore.domain.Store;

import com.coubee.coubeebestore.domain.StoreStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class StoreResponseDto {
    private Long storeId;
    private String storeName;       // 매장이름
    private String description;     // 매장 설명
    private String contactNo;       // 연락처
    private String storeAddress;     // 주소
    private String backImg;          // 배경 이미지 URL
    private String profileImg;      // 프로필 이미지 URL
//    private String storeTag;  // 카테고리
    private List<Category> storeTag;
}
