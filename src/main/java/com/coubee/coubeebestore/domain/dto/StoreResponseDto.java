package com.coubee.coubeebestore.domain.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.coubee.coubeebestore.domain.Store;

import lombok.Getter;
import lombok.Setter;

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
    private String category;  // 카테고리

    public static StoreResponseDto from(Store store) {

        StoreResponseDto dto = new StoreResponseDto();
        
        String categories = String.join(", ", store.getCategory());
        
        dto.setStoreName(store.getStoreName());
        dto.setDescription(store.getDescription());
        dto.setContactNo(store.getContactNo());
        dto.setStoreAddress(store.getStoreAddress());
        dto.setBackImg(store.getBackImg());
        dto.setProfileImg(store.getProfileImg());
        dto.setCategory(categories);

        return dto;
    }
}
