package com.coubee.coubeebestore.domain.dto;

import lombok.Data;

@Data
public class HotdealDto {
        private Long StoreId;
        private Double saleRate;
        private int maxDiscount;
}
