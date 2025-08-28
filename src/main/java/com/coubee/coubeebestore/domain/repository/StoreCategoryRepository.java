package com.coubee.coubeebestore.domain.repository;

import com.coubee.coubeebestore.domain.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {

    void deleteAllByStore_StoreId(Long storeId);
}
