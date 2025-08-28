package com.coubee.coubeebestore.domain.mapper;

import com.coubee.coubeebestore.domain.Category;
import com.coubee.coubeebestore.domain.dto.CategoryDto;
import com.coubee.coubeebestore.domain.dto.HotdealResponseDto;

public class HotdealMapper {
    public static HotdealResponseDto fromEntity(Hotdeal hotdeal) {
        return new HotdealResponseDto(hotdeal.getCategoryId(), hotdeal.getName());
    }
}
