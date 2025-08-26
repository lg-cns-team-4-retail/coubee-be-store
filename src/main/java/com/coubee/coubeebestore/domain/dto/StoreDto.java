package com.coubee.coubeebestore.domain.dto;

import com.coubee.coubeebestore.domain.StoreStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class StoreDto {

    private Long storeId;
    private Long ownerId;
    private String storeName;
    private String description;
    private String contactNo;
    private String storeAddress;
    private String workingHour;
    private double latitude;
    private double longitude;
    private String bizNo;
    private String backImg;
    private String profileImg;
    private String bizImg;
    private StoreStatus status;
    private LocalDateTime approvedAt;
    private String rejectReason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
//    private String storeTag;

    private List<CategoryDto> storeTag;


    private double distance;


}
