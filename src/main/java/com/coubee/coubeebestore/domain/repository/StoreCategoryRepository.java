package com.coubee.coubeebestore.domain.repository;

import com.coubee.coubeebestore.domain.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {

    void deleteAllByStore_StoreId(Long storeId);

    // 특정 매장의 카테고리 전체 조회
    @Query("SELECT sc FROM StoreCategory sc JOIN FETCH sc.category WHERE sc.store.storeId = :storeId")
    List<StoreCategory> findByStoreId(Long storeId);

    // 특정 카테고리 소속 매장 전체 조회
    @Query("SELECT sc FROM StoreCategory sc JOIN FETCH sc.store WHERE sc.category.categoryId = :categoryId")
    List<StoreCategory> findByCategoryId(Long categoryId);


    @Query("SELECT sc FROM StoreCategory sc JOIN FETCH sc.category WHERE sc.store.storeId IN :storeIds")
    List<StoreCategory> findByStoreIds(@Param("storeIds") List<Long> storeIds);
}
