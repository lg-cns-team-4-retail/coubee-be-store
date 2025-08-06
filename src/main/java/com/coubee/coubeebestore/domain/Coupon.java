package com.coubee.coubeebestore.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coupon extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 20, nullable = false)
    private String content;

    private float discountRate;
    private int maxDiscount;
//    private int amount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    @Setter
    private CouponStatus status;

    @Builder
    public Coupon(
        String title,
        String content,
        float discountRate,
        int maxDiscount,
//        int amount,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        this.title = title;
        this.content = content;
        this.discountRate = discountRate;
        this.maxDiscount = maxDiscount;
//        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = CouponStatus.ACTIVE;
    }
}