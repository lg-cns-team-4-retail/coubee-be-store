package com.coubee.coubeebestore.domain.dto;

import com.coubee.coubeebestore.domain.HotdealStatus;

import lombok.Data;

@Data
public class HotdealResponseDto {
        private Double saleRate;
        private int maxDiscount;
        private HotdealStatus hotdealStatus;
}
