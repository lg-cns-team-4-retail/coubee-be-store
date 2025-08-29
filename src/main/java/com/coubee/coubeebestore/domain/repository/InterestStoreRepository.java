package com.coubee.coubeebestore.domain.repository;

import com.coubee.coubeebestore.domain.InterestStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterestStoreRepository extends JpaRepository<InterestStore, Long> {

    Optional<InterestStore> findByUserIdAndStoreId(Long userId, Long storeId);

    List<InterestStore> findAllByUserId(Long userId);

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    List<InterestStore> findAllByStoreId(Long storeId);

    @Query("SELECT i.storeId FROM InterestStore i WHERE i.userId = :userId AND i.storeId IN :storeIds")
    List<Long> findStoreIdsByUserIdAndStoreIds(@Param("userId") Long userId,
                                               @Param("storeIds") List<Long> storeIds);

}
