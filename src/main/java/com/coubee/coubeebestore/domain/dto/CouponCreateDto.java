package com.coubee.coubeebestore.domain.dto;

import com.coubee.coubeebestore.domain.Store;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponCreateDto {
    private Long storeId;
    
    private String title;
    private String content;
    private float discountRate;
    private int maxDiscount;
//    private int amount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
