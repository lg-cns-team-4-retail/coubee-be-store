package com.coubee.coubeebestore.domain.mapper;

import com.coubee.coubeebestore.domain.Hotdeal;
import com.coubee.coubeebestore.domain.dto.HotdealResponseDto;

public class HotdealMapper {
    public static HotdealResponseDto fromEntity(Hotdeal hotdeal) {
        return new HotdealResponseDto(hotdeal.getSaleRate(), hotdeal.getMaxDiscount(), hotdeal.getHotdealStatus());
    }
}
