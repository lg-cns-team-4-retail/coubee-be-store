package com.coubee.coubeebestore.domain.repository;

import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreStatus;

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

    @Query(
    "SELECT s FROM Store s " +
    "LEFT JOIN s.category c " +
    "WHERE LOWER(s.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    "OR LOWER(c) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Store> findAllByKeyword(@Param("keyword") String keyword);

    Optional<Store> findByOwnerIdAndStoreId(Long ownerId, Long storeId);
}
