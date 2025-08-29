package com.coubee.coubeebestore.domain.mapper;

import com.coubee.coubeebestore.domain.HotdealStatus;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreCategory;
import com.coubee.coubeebestore.domain.dto.StoreDto;
import com.coubee.coubeebestore.domain.dto.StoreRegisterDto;
import com.coubee.coubeebestore.domain.dto.StoreResponseDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class StoreMapper {

    public static Store toEntity(Long ownerId, StoreRegisterDto dto){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point location = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
        location.setSRID(4326);
        return Store.builder()
                .ownerId(ownerId)
                .storeName(dto.getStoreName())
                .description(dto.getDescription())
                .contactNo(dto.getContactNo())
                .storeAddress(dto.getStoreAddress())
                .workingHour(dto.getWorkingHour())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .location(location)
                .bizNo(dto.getBizNo())
                .backImg(dto.getBackImg())
                .profileImg(dto.getProfileImg())
                .bizImg(dto.getBizImg())
                .build();
    }


    public static StoreDto fromEntity(Store store) {
        StoreDto dto = new StoreDto();
        dto.setStoreId(store.getStoreId());
        dto.setOwnerId(store.getOwnerId());
        dto.setStoreName(store.getStoreName());
        dto.setDescription(store.getDescription());
        dto.setContactNo(store.getContactNo());
        dto.setStoreAddress(store.getStoreAddress());
        dto.setWorkingHour(store.getWorkingHour());
        dto.setLatitude(store.getLatitude());
        dto.setLongitude(store.getLongitude());
        dto.setBizNo(store.getBizNo());
        dto.setBackImg(store.getBackImg());
        dto.setProfileImg(store.getProfileImg());
        dto.setBizImg(store.getBizImg());
        dto.setStatus(store.getStatus());
        dto.setApprovedAt(store.getApprovedAt());
        dto.setRejectReason(store.getRejectReason());
        dto.setCreatedAt(store.getCreatedAt());
        return dto;
    }
    public static StoreResponseDto fromEntity(Store store, boolean isInterest, double distance) {
        StoreResponseDto dto = fromEntityForUser(store, isInterest);
        dto.setDistance(distance);
        return dto;
    }

    public static StoreResponseDto fromEntityForUser(Store store, boolean isInterest) {
        StoreResponseDto dto = new StoreResponseDto();
        dto.setStoreId(store.getStoreId());
        dto.setStoreName(store.getStoreName());
        dto.setDescription(store.getDescription());
        dto.setContactNo(store.getContactNo());
        dto.setStoreAddress(store.getStoreAddress());
        dto.setWorkingHour(store.getWorkingHour());
        dto.setBackImg(store.getBackImg());
        dto.setProfileImg(store.getProfileImg());
        dto.setLongitude(store.getLongitude());
        dto.setLatitude(store.getLatitude());
        dto.setInterest(isInterest);
        return dto;
    }

    public static StoreResponseDto fromEntityForUser(Store store) {
        StoreResponseDto dto = new StoreResponseDto();
        dto.setStoreId(store.getStoreId());
        dto.setStoreName(store.getStoreName());
        dto.setDescription(store.getDescription());
        dto.setContactNo(store.getContactNo());
        dto.setStoreAddress(store.getStoreAddress());
        dto.setWorkingHour(store.getWorkingHour());
        dto.setBackImg(store.getBackImg());
        dto.setProfileImg(store.getProfileImg());
        dto.setLongitude(store.getLongitude());
        dto.setLatitude(store.getLatitude());
        return dto;
    }
}
