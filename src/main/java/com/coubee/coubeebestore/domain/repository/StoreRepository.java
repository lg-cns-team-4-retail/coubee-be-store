package com.coubee.coubeebestore.domain.repository;

import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.storeCategories sc LEFT JOIN FETCH sc.category WHERE s.ownerId = :ownerId")
    List<Store> findAllByOwnerId(Long ownerId);

    @Query(value = """
        SELECT s FROM Store s
        LEFT JOIN FETCH s.storeCategories sc
        LEFT JOIN FETCH sc.category
        WHERE s.status = com.coubee.coubeebestore.domain.StoreStatus.APPROVED
        AND function('ST_DistanceSphere',
            function('ST_MakePoint', :lng, :lat),
            function('ST_MakePoint', s.longitude, s.latitude)
        ) <= :maxDistance
        ORDER BY function('ST_DistanceSphere',
            function('ST_MakePoint', :lng, :lat),
            function('ST_MakePoint', s.longitude, s.latitude)
        )
    """)
    List<Store> findNearbyStoresOrderByDistance(@Param("lat") double lat, @Param("lng") double lng, @Param("maxDistance") double maxDistance);


    List<Store> findAllByStoreIdIn(List<Long> storeIds);

    Page<Store> findAll(Pageable pageable);

    List<Store> findAllByStatus(StoreStatus status);

    @Query("SELECT s FROM Store s JOIN FETCH s.storeCategories sc JOIN FETCH sc.category WHERE s.storeId = :id")
    Optional<Store> findStoreWithCategories(@Param("id") Long id);

}
