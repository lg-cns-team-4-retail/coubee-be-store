package com.coubee.coubeebestore.service;

import com.coubee.coubeebestore.common.exception.BadParameter;
import com.coubee.coubeebestore.common.exception.NotFound;
import com.coubee.coubeebestore.domain.*;
import com.coubee.coubeebestore.domain.dto.*;
import com.coubee.coubeebestore.domain.mapper.StoreMapper;
import com.coubee.coubeebestore.domain.repository.CategoryRepository;
import com.coubee.coubeebestore.domain.repository.InterestStoreRepository;
import com.coubee.coubeebestore.domain.repository.StoreCategoryRepository;
import com.coubee.coubeebestore.domain.repository.StoreRepository;
import com.coubee.coubeebestore.util.DistanceCalculator;
import com.coubee.coubeebestore.util.FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final FileUploader fileUploader;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    private final InterestStoreRepository interestStoreRepository;

    // 점주 기능
    // 매장 등록 요청
    @Transactional
    public void storeRegister(Long ownerId, StoreRegisterDto registerDto) {

        Store newStore = storeRepository.save(StoreMapper.toEntity(ownerId, registerDto));
        storeRepository.flush();
        List<String> tagNames = Arrays.stream(registerDto.getStoreTag().split(",")).toList();
        categoryRegister(newStore, tagNames);
    }

    public void categoryRegister(Store newStore, List<String> tagNames) {
        // 1. 기존 카테고리 한번에 조회
        List<Category> existingCategories = categoryRepository.findAllByNameIn(tagNames);
        Map<String, Category> categoryMap = existingCategories.stream()
                .collect(Collectors.toMap(Category::getName, c -> c));

        // 2. 없는 태그는 새로 Category 생성
        List<Category> newCategories = tagNames.stream()
                .filter(name -> !categoryMap.containsKey(name))
                .map(Category::new)
                .toList();
        List<Category> savedNewCategories = categoryRepository.saveAll(newCategories);
        // 3. 모든 Category 통합
        savedNewCategories.forEach(cat -> categoryMap.put(cat.getName(), cat));
        // 4. StoreCategory 저장
        List<StoreCategory> storeCategories = tagNames.stream()
                .map(tag -> StoreCategory.builder()
                        .store(newStore)
                        .category(categoryMap.get(tag))
                        .build())
                .toList();
        storeCategoryRepository.saveAll(storeCategories);
    }

    // 점주 소유 매장 리스트 조회
    public List<StoreDto> getStoreList(Long ownerId) {
        List<Store> stores = storeRepository.findAllByOwnerId(ownerId);
        return stores.stream()
                .map(StoreMapper::fromEntity)
                .collect(Collectors.toList());
    }

    // 점주 매장 상세 조회
    @Transactional(readOnly = true)
    public StoreDto getStoreById(Long ownerId, Long storeId) {
        Store store = storeRepository.findStoreWithCategories(storeId)
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        if (!Objects.equals(store.getOwnerId(), ownerId)) {
            throw new BadParameter("소유매장만 조회 가능");
        }
        return StoreMapper.fromEntity(store);
    }

    // 매장 정보 수정
    @Transactional
    public void storeUpdate(StoreUpdateDto storeUpdateDto) {
        Store store = storeRepository.findById(storeUpdateDto.getStoreId())
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        store.updateStore(storeUpdateDto);
        storeRepository.save(store);
    }

    /// 매장 등록시에 필요한 이미지 저장 1.백그라운드이미지 2.매장프로필이미지 3.사업자등록증이미지
    public String storeImgBackground(MultipartFile file) {
        return fileUploader.upload(file, "store/background");
    }

    public String storeImgProfile(MultipartFile file) {
        return fileUploader.upload(file, "store/profile");
    }

    public String storeImgCertificate(MultipartFile file) {
        return fileUploader.upload(file, "store/certificates");
    }


    // 관리자 기능
    // 매장 등록 승인
    @Transactional
    public void storeApprove(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        store.setStatus(StoreStatus.APPROVED);
        store.setApprovedAt(LocalDateTime.now());
        storeRepository.save(store);
    }

    // 매장 등록 거절
    @Transactional
    public void storeReject(Long storeId, StoreRejectDto dto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        store.setStatus(StoreStatus.REJECTED);
        store.setRejectReason(dto.getRejectReason());
        storeRepository.save(store);
    }

    // 매장 상태 PENDING,APPROVE,REJECT 별 목록 조회
    public List<StoreDto> getStatusList(StoreStatus status) {
        return storeRepository.findAllByStatus(status).stream().map(StoreMapper::fromEntity).toList();
    }

    // 매장 전체 조회 (page 처리)
    public Page<StoreDto> getStoreList(Pageable pageable) {
        Page<Store> stores = storeRepository.findAll(pageable);
        return stores.map(StoreMapper::fromEntity);
    }

    // 매장 삭제
    @Transactional
    public void storeDelete(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        storeRepository.delete(store);
    }

    // 일반 사용자 기능
    // 근처 매장 조회
    public List<StoreResponseDto> getNearStoreList(Double latitude, Double longitude) {
        return storeRepository.findNearbyStoresOrderByDistance(latitude, longitude, 500)
                .stream()
                .map(store -> {
                    double distance = DistanceCalculator.calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude());
                    return StoreMapper.fromEntity(store, distance);
                })
                .toList();
    }

    // 관심 매장 등록
    @Transactional
    public void addInterestStore(Long userId, Long storeId) {
        storeRepository.findById(storeId).orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        Optional<InterestStore> exist = interestStoreRepository.findByUserIdAndStoreId(userId, storeId);
        if (exist.isPresent()) {
            interestStoreRepository.delete(exist.get());
        } else {
            InterestStore interest = InterestStore.builder()
                    .userId(userId)
                    .storeId(storeId)
                    .build();
            interestStoreRepository.save(interest);
        }
    }

    // 관심 매장 목록 조회
    public List<Store> getMyInterestStores(Long userId) {
        List<InterestStore> interestList = interestStoreRepository.findByUserId(userId);
        List<Long> storeIds = interestList.stream().map(InterestStore::getStoreId).toList();
        return storeRepository.findAllById(storeIds);
    }

    // 매장 상세 조회
    @Transactional(readOnly = true)
    public StoreResponseDto getStoreById(Long storeId) {
        Store store = storeRepository.findStoreWithCategories(storeId)
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        if (store.getStatus().equals(StoreStatus.PENDING)) {
            throw new BadParameter("아직 승인을 기다리고 있는 매장입니다");
        } else if (store.getStatus().equals(StoreStatus.REJECTED)) {
            throw new BadParameter("승인 거절된 매장입니다");
        }
        return StoreMapper.fromEntityForUser(store);
    }


}
