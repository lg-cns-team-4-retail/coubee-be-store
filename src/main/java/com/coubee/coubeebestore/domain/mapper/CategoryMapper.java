package com.coubee.coubeebestore.domain.mapper;

import com.coubee.coubeebestore.domain.Category;
import com.coubee.coubeebestore.domain.StoreCategory;
import com.coubee.coubeebestore.domain.dto.CategoryDto;

public class CategoryMapper {
    public static CategoryDto fromEntity(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName());
    }

}