package com.coubee.coubeebestore.service;

import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.dto.StoreRegisterDto;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final FileUploader fileUploader;
    private final StoreRepository storeRepository;
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


}
