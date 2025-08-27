package com.coubee.coubeebestore.service;

import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.dto.StoreResponseDto;
import com.coubee.coubeebestore.domain.mapper.StoreMapper;
import com.coubee.coubeebestore.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BackendStoreService {

    private final StoreRepository storeRepository;

    public List<Long> getNearStoreIds(double latitude, double longitude, String keyword) {
        return storeRepository.findNearbyStoresOrderByDistance(latitude, longitude, 500, keyword)
                .stream()
                .map(Store::getStoreId)
                .toList();
    }

    public boolean validStoreId(Long storeId) {
        if (storeId == null) {
            return false;
        }
        return storeRepository.existsById(storeId);
    }

    public Map<Long, StoreResponseDto> getStoresByIds(List<Long> storeIds) {
        if (storeIds == null || storeIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return storeRepository.findAllByStoreIdIn(storeIds).stream()
                .map(StoreMapper::fromEntityForUser)
                .collect(Collectors.toMap(StoreResponseDto::getStoreId, Function.identity()));
    }
}

