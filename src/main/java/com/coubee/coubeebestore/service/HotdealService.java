package com.coubee.coubeebestore.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.coubee.coubeebestore.common.exception.BadParameter;
import com.coubee.coubeebestore.common.exception.NotFound;
import com.coubee.coubeebestore.domain.Hotdeal;
import com.coubee.coubeebestore.domain.HotdealStatus;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.dto.HotdealDto;
import com.coubee.coubeebestore.domain.repository.HotdealRepository;
import com.coubee.coubeebestore.domain.repository.StoreRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotdealService {

    private final StoreRepository storeRepository;
    private final HotdealRepository hotdealRepository;

    @Transactional
    public void onHotdeal(Long adminId, HotdealDto dto) {
        Store store = storeRepository.findById(dto.getStore().getStoreId())
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        if (!Objects.equals(store.getOwnerId(), adminId)) {
            throw new BadParameter("소유매장만 적용 가능");
        }

        // 기존에 켜져 있는 핫딜이 있으면 먼저 끈다
        hotdealRepository.findByStoreIdAndStatusOn(store.getStoreId())
                .ifPresent(h -> h.setHotdealStatus(HotdealStatus.OFF));

        // 새로운 핫딜 생성
        Hotdeal hotdeal = Hotdeal.builder()
                    .store(store)
                    .saleRate(dto.getSaleRate())
                    .maxDiscount(dto.getMaxDiscount())
                    .build();

        hotdealRepository.save(hotdeal);
        hotdealRepository.flush();
    }

    @Transactional
    public void offHotdeal(Long adminId, HotdealDto dto) {
        Store store = storeRepository.findById(dto.getStore().getStoreId())
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        if (!Objects.equals(store.getOwnerId(), adminId)) {
            throw new BadParameter("소유매장만 적용 가능");
        }

        Hotdeal hotdeal = hotdealRepository.findByStoreId(store.getStoreId())
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        hotdeal.setHotdealStatus(HotdealStatus.OFF);

        hotdealRepository.save(hotdeal);
        hotdealRepository.flush();
    }
}
