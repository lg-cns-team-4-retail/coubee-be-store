package com.coubee.coubeebestore.domain;

import java.time.LocalDateTime;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.path_function_return;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CouponRedemption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Coupon coupon;

    @Getter
    private Long couponId;

    @Getter
    @Setter
    private Long userId;

    private LocalDateTime redemptAt;

    private LocalDateTime usedAt;

    @Builder
    public CouponRedemption
    (
        Store store,
        Coupon coupon,
        Long userId,
        LocalDateTime redemptAt,
        LocalDateTime usedAt
    ) {
        this.store = store;
        this.coupon = coupon;
        this.couponId = coupon.getCouponId();
        this.userId = userId;
        this.redemptAt = redemptAt;
        this.usedAt = usedAt;
    }

    public void use() {
        if(!coupon.isExpired()) {
        coupon.setStatus(CouponStatus.INACTIVE);
        this.usedAt = LocalDateTime.now();
        }
    }

    // public void allocate(Long userId, Coupon coupon) {
    //     if(coupon.getAmount() != 0) {
    //         this.coupon = coupon;
    //         this.store = coupon.getStore();
    //         this.userId = userId;
    //         this.redemptAt = LocalDateTime.now();
    //     }
    // }
}
