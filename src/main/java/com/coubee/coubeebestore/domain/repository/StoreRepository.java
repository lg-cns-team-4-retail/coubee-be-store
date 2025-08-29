package com.coubee.coubeebestore.domain.repository;

import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreStatus;
import com.coubee.coubeebestore.domain.dto.HotdealResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE s.ownerId = :ownerId")
    List<Store> findAllByOwnerId(Long ownerId);

    @Query(value = """
    SELECT s.storeId FROM Store s 
    WHERE s.ownerId = :ownerId 
    AND s.status = com.coubee.coubeebestore.domain.StoreStatus.APPROVED
    """)
    List<Long> findAllByOwnerIdOnApproved(Long ownerId);

    @Query(value = """
        SELECT s FROM Store s
        WHERE s.status = com.coubee.coubeebestore.domain.StoreStatus.APPROVED
        AND UPPER(s.storeName) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND function('ST_DistanceSphere',
            function('ST_MakePoint', :lng, :lat),
            function('ST_MakePoint', s.longitude, s.latitude)
        ) <= :maxDistance 
        ORDER BY function('ST_DistanceSphere',
            function('ST_MakePoint', :lng, :lat),
            function('ST_MakePoint', s.longitude, s.latitude)
        )
    """)
    List<Store> findNearbyStoresOrderByDistanceAndKeyword(@Param("lat") double lat, @Param("lng") double lng, @Param("maxDistance") double maxDistance, @Param("keyword") String keyword);

    @Query("""
      SELECT DISTINCT s
      FROM Store s
      WHERE UPPER(s.storeName) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND (:status IS NULL OR s.status = :status)
        ORDER BY s.createdAt desc
    """)
    List<Store> findByKeywordAndOptionalStatusWithGraph(@Param("keyword") String keyword,
                                                        @Param("status") StoreStatus status);

    @Query("""
    SELECT DISTINCT s
    FROM Store s
    WHERE s.storeId IN :storeIds
    """)                                                    
    List<Store> findAllByStoreIdIn(List<Long> storeIds);

    Page<Store> findAll(Pageable pageable);

    List<Store> findAllByStatus(StoreStatus status);

    @Query("SELECT s.status FROM Store s WHERE s.storeId = :storeId")
    StoreStatus findStatusByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT s FROM Store s WHERE s.storeId = :id")
    Optional<Store> findStoreWithCategories(@Param("id") Long id);

        @Query(value = """
        SELECT s FROM Store s
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
}
