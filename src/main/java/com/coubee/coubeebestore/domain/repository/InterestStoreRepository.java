package com.coubee.coubeebestore.domain.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coubee.coubeebestore.domain.InterestStore;
import com.coubee.coubeebestore.domain.Store;

@FeignClient(name = "coubee-be-user")
public interface InterestStoreRepository extends JpaRepository<InterestStore, Long> {

    boolean existsByUserIdAndStore(Long userId, Store store);

    List<InterestStore> findByUserId(Long userId);

}
