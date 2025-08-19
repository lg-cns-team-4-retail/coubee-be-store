package com.coubee.coubeebestore.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import com.coubee.coubeebestore.domain.Coupon;

import feign.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface CouponRepository extends JpaRepository<Coupon, Long>{
    List<Coupon> findAllByStore_StoreId(Long storeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Stock c where c.id = :couponId")
    @QueryHints({ @QueryHint(name = "javax.persistence.lock.timeout", value = "3000") })
    Optional<Coupon> findByCouponId(@Param("couponId") Long couponId);
}
