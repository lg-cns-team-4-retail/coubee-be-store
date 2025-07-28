package com.coubee.coubeebestore.service;

import com.coubee.coubeebestore.common.exception.NotFound;
import com.coubee.coubeebestore.domain.InterestStore;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreStatus;
import com.coubee.coubeebestore.domain.dto.StoreDto;
import com.coubee.coubeebestore.domain.dto.StoreRegisterDto;
import com.coubee.coubeebestore.domain.repository.InterestStoreRepository;
import com.coubee.coubeebestore.domain.repository.StoreRepository;
import com.coubee.coubeebestore.util.FileUploader;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final FileUploader fileUploader;
    private final StoreRepository storeRepository;
    private final InterestStoreRepository interestStoreRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional
    public void storeRegister(Long ownerId, StoreRegisterDto registerDto) {
        Point location = geometryFactory.createPoint(new Coordinate(registerDto.getLongitude(), registerDto.getLatitude()));
        location.setSRID(4326);
        Store newStore = Store.builder()
                .ownerId(ownerId)
                .storeName(registerDto.getStoreName())
                .description(registerDto.getDescription())
                .contactNo(registerDto.getContactNo())
                .storeAddress(registerDto.getStoreAddress())
                .latitude(registerDto.getLatitude())
                .longitude(registerDto.getLongitude())
                .location(location)
                .bizNo(registerDto.getBizNo())
                .backImg(registerDto.getBackImg())
                .profileImg(registerDto.getProfileImg())
                .build();
        storeRepository.save(newStore);
    }

    @Transactional
    public void storeApprove(StoreDto storeDto) {
        Store store = storeRepository.findByStoreIdandStoreName(storeDto.getStoreId(), storeDto.getStoreName())
            .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));

        store.setStatus(StoreStatus.APPROVED);
        store.setApprovedAt(LocalDateTime.now());
        storeRepository.save(store);
    }

    public List<Store> getStoreList(Long ownerId) {
        return storeRepository.findAllByOwnerId(ownerId);
    }

    public String storeImgBackground(MultipartFile file) {
        return fileUploader.upload(file, "store/background");
    }

    public String storeImgProfile(MultipartFile file) {
        return fileUploader.upload(file, "store/profile");
    }

    public List<Store> getNearStoreList(Double latitude, Double longitude) {
        return storeRepository.findNearbyStoresOrderByDistance(latitude, longitude);
    }

    public void addInterestStore(Long userId, StoreDto storeDto) {

        Store store = storeRepository.findById(storeDto.getStoreId())
                      .orElseThrow(() -> new IllegalArgumentException("매장 없음"));

        boolean exists = interestStoreRepository.existsByUserIdAndStore(userId, store);
        if (exists) {
            throw new IllegalStateException("이미 관심 매장으로 등록됨");
        }

        InterestStore interest = InterestStore.builder()
            .userId(userId)
            .store(store)
            .build();
        interestStoreRepository.save(interest);
    }

}
