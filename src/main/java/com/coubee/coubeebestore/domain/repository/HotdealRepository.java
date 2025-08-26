package com.coubee.coubeebestore.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coubee.coubeebestore.domain.Hotdeal;

public interface HotdealRepository extends JpaRepository<Hotdeal, Long>{
    
    Optional<Hotdeal> findByStoreIdAndStatusOn(Long storeId);

    Optional<Hotdeal> findByStoreId(Long storeId);

}
