package com.coubee.coubeebestore.domain.mapper;

import com.coubee.coubeebestore.domain.Coupon;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.dto.CouponCreateDto;
import com.coubee.coubeebestore.domain.dto.CouponResponseDto;


public class CouponMapper {
    public static Coupon toEntity(Store store, CouponCreateDto dto) {
        return Coupon.builder()
                .store(store)
                .title(dto.getTitle())
                .content(dto.getContent())
                .discountRate(dto.getDiscountRate())
                .maxDiscount(dto.getMaxDiscount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    public static CouponResponseDto fromEntity(Coupon coupon) {
        CouponResponseDto dto = new CouponResponseDto();
        dto.setTitle(coupon.getTitle());
        dto.setStoreName(coupon.getStore().getStoreName());
        dto.setContent(coupon.getContent());
        dto.setDiscountRate(coupon.getDiscountRate());
        dto.setMaxDiscount(coupon.getMaxDiscount());
        dto.setStartDate(coupon.getStartDate());
        dto.setEndDate(coupon.getEndDate());
        return dto;
    }

}