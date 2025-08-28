package com.coubee.coubeebestore.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coubee.coubeebestore.domain.Hotdeal;
import com.coubee.coubeebestore.domain.HotdealStatus;
import com.coubee.coubeebestore.domain.dto.HotdealResponseDto;

public interface HotdealRepository extends JpaRepository<Hotdeal, Long>{
    
    Optional<Hotdeal> findByStore_StoreIdAndHotdealStatus(Long storeId, HotdealStatus hotdealStatus);

    Hotdeal findByStore_StoreId(Long storeId);

    @Query("""
    SELECT h FROM Hotdeal h
    WHERE h.store.storeId = :storeId
    AND h.hotdealStatus = com.coubee.coubeebestore.domain.HotdealStatus.ACTIVE
    """)
    Optional<HotdealResponseDto> findHotdealByStoreId(@Param("storeId") Long storeId);
}
