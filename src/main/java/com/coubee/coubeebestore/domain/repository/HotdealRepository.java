package com.coubee.coubeebestore.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coubee.coubeebestore.domain.Hotdeal;
import com.coubee.coubeebestore.domain.HotdealStatus;

public interface HotdealRepository extends JpaRepository<Hotdeal, Long>{
    
    Optional<Hotdeal> findByStore_StoreIdAndHotdealStatus(Long storeId, HotdealStatus hotdealStatus);
}
