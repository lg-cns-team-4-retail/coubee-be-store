package com.coubee.coubeebestore.service;

import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BackendStoreService {

    private final StoreRepository storeRepository;

    public List<Long> getNearStoreIds(double latitude, double longitude) {
        return storeRepository.findNearbyStoresOrderByDistance(latitude, longitude, 500)
                .stream()
                .map(Store::getStoreId)
                .toList();
    }
}
