package com.coubee.coubeebestore.domain.dto;

import com.coubee.coubeebestore.domain.Coupon;
import com.coubee.coubeebestore.domain.Store;

import lombok.Data;

@Data
public class CouponRegisterDto {
    private Long couponId;
    private Long userId;
    private Store store;
    private Coupon coupon;
}
