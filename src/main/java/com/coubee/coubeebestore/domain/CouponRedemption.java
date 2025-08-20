package com.coubee.coubeebestore.domain;

import java.time.LocalDateTime;

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
@Table(name = "couponRedemption")
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
        this.validate();
        coupon.setStatus(CouponStatus.USED);
        this.usedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(coupon.getEndDate());
    }

    public void validate() {
        if(isExpired()){
            throw new IllegalArgumentException("사용이 만료된 쿠폰입니다.");
        }

        if(coupon.getStatus() == CouponStatus.INACTIVE) {
            throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
        }

        if(coupon.getStatus() == CouponStatus.USED) {
            throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
        }
    }

    // public void allocate(Long userId, Coupon coupon) {
    //     if(coupon.getAmount() != 0) {
    //         this.coupon = coupon;
    //         this.store = coupon.getStore();
    //         this.userId = userId;
    //         this.redemptAt = LocalDateTime.now();
    //         coupon.setAmount(coupon.getAmount() - 1);
    //     }
    // }
}
