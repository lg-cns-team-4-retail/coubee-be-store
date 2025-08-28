package com.coubee.coubeebestore.domain.dto;

import com.coubee.coubeebestore.domain.HotdealStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotdealResponseDto {
        private Double saleRate;
        private int maxDiscount;
        private HotdealStatus hotdealStatus;
}
