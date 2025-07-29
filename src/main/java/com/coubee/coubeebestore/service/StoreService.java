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
import java.util.Optional;

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
    public void storeApprove(Long storeId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        store.setStatus(StoreStatus.APPROVED);
        store.setApprovedAt(LocalDateTime.now());
        storeRepository.save(store);
    }
    @Transactional
    public void storeReject(Long storeId,String rejectReason) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        store.setStatus(StoreStatus.REJECTED);
        store.setRejectReason(rejectReason);
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
    public String storeImgCertificate(MultipartFile file) {
        return fileUploader.upload(file, "store/certificates");
    }

    public List<Store> getNearStoreList(Double latitude, Double longitude) {
        return storeRepository.findNearbyStoresOrderByDistance(latitude, longitude);
    }

    @Transactional
    public void addInterestStore(Long userId, Long storeId) {
        storeRepository.findById(storeId).orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        Optional<InterestStore> exist= interestStoreRepository.findByUserIdAndStoreId(userId, storeId);
        if (exist.isPresent()) {
            interestStoreRepository.delete(exist.get());
        }else{
            InterestStore interest = InterestStore.builder()
                    .userId(userId)
                    .storeId(storeId)
                    .build();
            interestStoreRepository.save(interest);
        }
    }
    public List<Store> getMyInterestStores(Long userId){
        List<InterestStore> interestList = interestStoreRepository.findByUserId(userId);
        List<Long> storeIds = interestList.stream().map(InterestStore::getStoreId).toList();
        return storeRepository.findAllById(storeIds);
    }

    public List<Store> getStatusList(StoreStatus status) {
        return storeRepository.findAllByStatus(status);
    }

    @Transactional
    public void storeUpdate(Long storeId, StoreDto store) {
        Store newStore = storeRepository.findByStoreId(storeId).get();
        newStore.setStoreName(store.getStoreName());
        newStore.setDescription(store.getDescription());
        newStore.setContactNo(store.getContactNo());
        newStore.setProfileImg(store.getProfileImg());
        newStore.setBackImg(store.getBackImg());
        newStore.getUpdatedAt();
        storeRepository.save(newStore);
    }

    @Transactional
    public void storeDelete(Long storeId) {
                Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
            storeRepository.delete(store);
    }    
}
