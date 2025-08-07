package com.coubee.coubeebestore.domain.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CouponResponseDto {
    private String title;
    private String content;
    private float discountRate;
    private int maxDiscount;
//    private int amount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String storeName;
    
}
