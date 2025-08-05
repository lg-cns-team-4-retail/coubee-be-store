package com.coubee.coubeebestore.domain.dto;

import com.coubee.coubeebestore.domain.Store;

import lombok.Data;

@Data
public class CouponCreateDto {
    private Long storeId;
    
    private String title;
    private String content;
    private float discountRate;
    private int maxDiscount;
//    private int amount;

    private String startDate;
    private String endDate;
}
