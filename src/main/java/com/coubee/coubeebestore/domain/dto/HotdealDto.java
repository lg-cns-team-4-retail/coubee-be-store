package com.coubee.coubeebestore.domain.dto;

import java.time.LocalDateTime;

import com.coubee.coubeebestore.domain.HotdealStatus;
import com.coubee.coubeebestore.domain.Store;

import lombok.Data;

@Data
public class HotdealDto {
        private Store store;
        private Double saleRate;
        private int maxDiscount;
        private HotdealStatus hotdealStatus;
        private LocalDateTime createdAt;
        private LocalDateTime deletedAt;
}
