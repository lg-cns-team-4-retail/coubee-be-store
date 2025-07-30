package com.coubee.coubeebestore.domain.repository;


import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreStatus;
import com.coubee.coubeebestore.domain.dto.StoreDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByOwnerId(Long ownerId);

    @Query(value = """
    SELECT *
    FROM coubee_store.store s
    WHERE ST_DWithin(
        s.location,
        ST_SetSRID(ST_MakePoint(:lng, :lat), 4326),
        500
    )
    ORDER BY ST_Distance(
        s.location,
        ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)
    )
    """, nativeQuery = true)
    List<Store> findNearbyStoresOrderByDistance(@Param("lat") double lat, @Param("lng") double lng);

    List<Store> findAllByStoreIdIn(List<Long> storeIds);

    List<Store> findAllByStatus(StoreStatus status);

    Optional<Store> findByStoreId(Long storeId);

    @Query(
    "SELECT s FROM StoreEntity s " +
    "LEFT JOIN s.tags t " +
    "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    "OR LOWER(t) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Store> findAllByKeyword(@Param("keyword") String keyword);

}
