package com.coubee.coubeebestore.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coubee.coubeebestore.common.exception.NotFound;
import com.coubee.coubeebestore.domain.Coupon;
import com.coubee.coubeebestore.domain.CouponRedemption;
import com.coubee.coubeebestore.domain.CouponStatus;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.dto.CouponCreateDto;
import com.coubee.coubeebestore.domain.dto.CouponResponseDto;
import com.coubee.coubeebestore.domain.mapper.CouponMapper;
import com.coubee.coubeebestore.domain.repository.CouponRedemptionRepository;
import com.coubee.coubeebestore.domain.repository.CouponRepository;
import com.coubee.coubeebestore.domain.repository.StoreRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {
    
    private final CouponRedemptionRepository couponRedemptionRepository;
    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;

    // 점주 기능
    // 1. 쿠폰 생성
    public void createCoupon(CouponCreateDto couponCreateDto) {
        Store store = storeRepository.findById(couponCreateDto.getStoreId())
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        
        Coupon coupon = Coupon.builder()
                .store(store)
                .title(couponCreateDto.getTitle())
                .content(couponCreateDto.getContent())
                .discountRate(couponCreateDto.getDiscountRate())
                .maxDiscount(couponCreateDto.getMaxDiscount())
//                .amount(couponCreateDto.getAmount())
                .startDate(couponCreateDto.getStartDate())
                .endDate(couponCreateDto.getEndDate())
                .build();

        couponRepository.save(coupon);
    }

    // coupon 조회시 enddate가 지났을때 Expired로 상태 변경후 리턴
    // 2. 매장별 쿠폰 조회
    @Transactional
    public List<Coupon> getCouponList(Long storeId) {
        List<Coupon> couponList = couponRepository.findAllByStore_StoreId(storeId);
        for(Coupon coupon : couponList) {
            if(coupon.isExpired()) {
                coupon.setStatus(CouponStatus.EXPIRED);
                couponRepository.save(coupon);
            }
        }
        return couponList;
    }

    // 3. 쿠폰 삭제
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFound("해당 쿠폰을 찾을 수 없습니다."));
        couponRepository.delete(coupon);
    }

    // 4. 쿠폰 상세 조회
    public CouponResponseDto viewCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new NotFound("해당 쿠폰을 찾을 수 없습니다.")); 
        return CouponMapper.fromEntity(coupon);
    }

    // 일반 사용자 기능
    // 1. 쿠폰 등록
    @Transactional
    public void IssueAndRegisterCoupon(Long userId, Long couponId) {
        
        // 1) 비관적 락 적용
        Coupon coupon = couponRepository.findByCouponId(couponId)
            .orElseThrow(() -> new NotFound("해당 쿠폰을 찾을 수 없습니다."));
           // 2) 수량 체크
        // if (coupon.getAmount() <= 0) {
        //     throw new IllegalStateException("쿠폰 재고가 없습니다.");
        // }

        // 3) 이미 발급받았는지 확인 (중복 방지)
        boolean alreadyIssued = couponRedemptionRepository.existsByCoupon_CouponIdAndUserId(couponId, userId);
        if (alreadyIssued) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }

        CouponRedemption couponRedemption = CouponRedemption.builder()
            .store(coupon.getStore())
            .coupon(coupon)
            .userId(userId)
            .redemptAt(LocalDateTime.now())
            .build();
        couponRedemptionRepository.save(couponRedemption);
        // coupon.setAmount(coupon.getAmount()-1); // 발급 쿠폰량 -1
        // couponRepository.save(coupon);
    }

    // 2. 내 쿠폰 조회
    public List<Coupon> getMyCouponList(Long userId) {
        List<CouponRedemption> couponList = couponRedemptionRepository.findByUserId(userId);
        List<Long> couponIds = couponList
                    .stream()
                    .map(CouponRedemption::getCouponId)
                    .toList();
        return couponRepository.findAllByCouponIdIn(couponIds);
    }

    // 3. 쿠폰 사용
    public void useCoupon(Long couponId) {
        CouponRedemption myCoupon = couponRedemptionRepository.findByCouponId(couponId)
            .orElseThrow(() -> new NotFound("해당 쿠폰을 찾을 수 없습니다."));
        myCoupon.use();
    }
    
}
