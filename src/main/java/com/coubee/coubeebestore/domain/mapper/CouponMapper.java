package com.coubee.coubeebestore.domain.mapper;

import com.coubee.coubeebestore.domain.Coupon;
import com.coubee.coubeebestore.domain.dto.CouponResponseDto;

public class CouponMapper {

    public static CouponResponseDto fromEntity(Coupon coupon) {
        CouponResponseDto dto = fromEntityForUser(coupon);
        return dto; 
    }

    public static CouponResponseDto fromEntityForUser(Coupon coupon) {
        CouponResponseDto dto = new CouponResponseDto();
        return dto; 
    }
}