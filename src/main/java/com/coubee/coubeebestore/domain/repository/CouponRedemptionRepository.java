package com.coubee.coubeebestore.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coubee.coubeebestore.domain.CouponRedemption;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long>{
    List<CouponRedemption> findByUserId(Long userId);
    Optional<CouponRedemption> findByCouponId(Long couponId);
    boolean existsByCoupon_CouponIdAndUserId(Long userId, Long couponId);
}
