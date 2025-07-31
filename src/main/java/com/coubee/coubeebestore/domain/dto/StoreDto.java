package com.coubee.coubeebestore.domain.dto;

import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoreDto {

    private Long storeId;
    private Long ownerId;
    private String storeName;
    private String description;
    private String contactNo;
    private String storeAddress;
    private double latitude;
    private double longitude;
    private String bizNo;
    private String backImg;
    private String profileImg;
    private StoreStatus status;
    private LocalDateTime approvedAt;
    private String rejectReason;
    private String category;

    public static StoreDto from(Store store) {

        StoreDto dto = new StoreDto();
        
        String categories = String.join(", ", store.getCategory());
        
        dto.setStoreName(store.getStoreName());
        dto.setDescription(store.getDescription());
        dto.setContactNo(store.getContactNo());
        dto.setStoreAddress(store.getStoreAddress());
        dto.setLatitude(store.getLatitude());
        dto.setLongitude(store.getLongitude());
        dto.setBizNo(store.getBizNo());
        dto.setBackImg(store.getBackImg());
        dto.setProfileImg(store.getProfileImg());
        dto.setCategory(categories);

        return dto;
    }
}
