package com.coubee.coubeebestore.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coubee.coubeebestore.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>{
    List<Coupon> findAllByStoreId(Long storeId);
}
