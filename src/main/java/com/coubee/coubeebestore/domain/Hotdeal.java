package com.coubee.coubeebestore.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "hotdeal")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hotdeal extends BaseTimeEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private Double saleRate;

    private int maxDiscount;

    @ColumnDefault("HotdealStatus.OFF")
    private HotdealStatus hotdealStatus;

    private LocalDateTime deletedAt;

    @Builder
    public Hotdeal(
        Store store,
        Double saleRate,
        int maxDiscount,
        HotdealStatus hotdealStatus
    ) {
        this.store = store;
        this.saleRate = saleRate;
        this.maxDiscount = maxDiscount;
        this.hotdealStatus = hotdealStatus;
    }
}
