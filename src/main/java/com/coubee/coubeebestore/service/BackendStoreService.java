package com.coubee.coubeebestore.service;

import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreStatus;
import com.coubee.coubeebestore.domain.dto.HotdealResponseDto;
import com.coubee.coubeebestore.domain.dto.StoreResponseDto;
import com.coubee.coubeebestore.domain.mapper.HotdealMapper;
import com.coubee.coubeebestore.domain.mapper.StoreMapper;
import com.coubee.coubeebestore.domain.repository.HotdealRepository;
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
    private final HotdealRepository hotdealRepository;

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
                .map(store -> StoreMapper.fromEntityForUser(store))
                .collect(Collectors.toMap(StoreResponseDto::getStoreId, Function.identity()));
    }

    public List<Long> getStoresByOwnerIdOnApproved(Long ownerId) {
        return storeRepository.findAllByOwnerIdOnApproved(ownerId);
    }

    public boolean validStatusByStoreId(Long storeId) {
        StoreStatus status = storeRepository.findStatusByStoreId(storeId);
        if (status == StoreStatus.APPROVED) {
            return true;
        }
        return false;
    }

    public HotdealResponseDto getHotdeal(Long storeId) {
        return hotdealRepository.findHotdealByStoreId(storeId)
                    .map(HotdealMapper::fromEntity)
                    .orElse(null);
    }
}

