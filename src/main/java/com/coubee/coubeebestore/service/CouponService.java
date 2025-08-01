package com.coubee.coubeebestore.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coubee.coubeebestore.common.exception.NotFound;
import com.coubee.coubeebestore.domain.Coupon;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.dto.CouponCreateDto;
import com.coubee.coubeebestore.domain.repository.CouponRepository;
import com.coubee.coubeebestore.domain.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;

    public void createCoupon(CouponCreateDto couponCreateDto) {
        Store store = storeRepository.findById(couponCreateDto.getStoreId())
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        
        Coupon coupon = Coupon.builder()
                .store(store)
                .title(couponCreateDto.getTitle())
                .content(couponCreateDto.getContent())
                .discountRate(couponCreateDto.getDiscountRate())
                .maxDiscount(couponCreateDto.getMaxDiscount())
                .amount(couponCreateDto.getAmount())
                .startDate(LocalDateTime.parse(couponCreateDto.getStartDate()))
                .endDate(LocalDateTime.parse(couponCreateDto.getEndDate()))
                .build();

        couponRepository.save(coupon);
    }

    public List<Coupon> getCouponList(Long storeId) {
        return couponRepository.findAllByStoreId(storeId);
    }


}
