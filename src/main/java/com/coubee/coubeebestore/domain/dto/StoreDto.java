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
//    private String storeTag;

    private List<Category> storeTag;


    private double distance;


}
