package com.coubee.coubeebestore.service;

import com.coubee.coubeebestore.common.exception.BadParameter;
import com.coubee.coubeebestore.common.exception.NotFound;
import com.coubee.coubeebestore.common.web.context.GatewayRequestHeaderUtils;
import com.coubee.coubeebestore.domain.*;
import com.coubee.coubeebestore.domain.dto.*;
import com.coubee.coubeebestore.domain.mapper.CategoryMapper;
import com.coubee.coubeebestore.domain.mapper.HotdealMapper;
import com.coubee.coubeebestore.domain.mapper.StoreMapper;
import com.coubee.coubeebestore.domain.repository.CategoryRepository;
import com.coubee.coubeebestore.domain.repository.HotdealRepository;
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
    private final HotdealRepository hotdealRepository;

    // 점주 기능
    // 매장 등록 요청
    @Transactional
    public void storeRegister(Long ownerId, StoreRegisterDto registerDto) {

        Store newStore = storeRepository.save(StoreMapper.toEntity(ownerId, registerDto));
        storeRepository.flush();
        List<String> tagNames = Arrays.stream(registerDto.getStoreTag().split(","))
                .map(String::trim).distinct().toList();
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
        storeCategoryRepository.flush();
    }

    public void storeCategoryDelete(Long storeId){
        storeCategoryRepository.deleteAllByStore_StoreId(storeId);
        storeCategoryRepository.flush();
    }

    // 점주 소유 매장 리스트 조회
    public List<StoreDto> getStoreList(Long ownerId) {
        // 1. Store 목록 조회
        List<Store> stores = storeRepository.findAllByOwnerId(ownerId);
        List<Long> storeIds = stores.stream().map(Store::getStoreId).toList();

        // 2. Category, Hotdeal 한번에 가져오기
        List<StoreCategory> storeCategories = storeCategoryRepository.findByStoreIds(storeIds);
        List<Hotdeal> hotdeals = hotdealRepository.findByStoreIds(storeIds);

        // 3. storeId 기준으로 그룹핑
        Map<Long, List<CategoryDto>> categoryMap = storeCategories.stream()
                .collect(Collectors.groupingBy(sc -> sc.getStore().getStoreId(),
                        Collectors.mapping(sc -> CategoryMapper.fromEntity(sc.getCategory()), Collectors.toList())));

        Map<Long, HotdealResponseDto> hotdealMap = hotdeals.stream()
                .collect(Collectors.toMap(h -> h.getStore().getStoreId(),
                        HotdealMapper::fromEntity,
                        (h1, h2) -> h1)); // 중복시 첫번째만

        // 4. 최종 DTO 변환
        return stores.stream()
                .map(store -> {
                    StoreDto dto = StoreMapper.fromEntity(store);
                    dto.setStoreTag(categoryMap.getOrDefault(store.getStoreId(), List.of()));
                    dto.setHotdeal(hotdealMap.get(store.getStoreId()));
                    return dto;
                })
                .toList();
    }

    // 점주 매장 상세 조회
    @Transactional(readOnly = true)
    public StoreDto getStoreById(Long ownerId, Long storeId) {
        Store store = storeRepository.findStoreWithCategories(storeId)
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));

        // 3. StoreCategory 조회 후 DTO 변환
        List<CategoryDto> categories = storeCategoryRepository.findByStoreId(storeId).stream()
                .map(sc -> CategoryMapper.fromEntity(sc.getCategory()))
                .toList();
        // 4. Hotdeal 조회 후 DTO 변환
        Optional<Hotdeal> hotdeal = hotdealRepository.findHotdealByStoreId(storeId);
        HotdealResponseDto hotdealDto = hotdeal.isPresent() ? HotdealMapper.fromEntity(hotdeal.get()) : null;

        StoreDto storeDto = StoreMapper.fromEntity(store);
        storeDto.setStoreTag(categories);
        storeDto.setHotdeal(hotdealDto);
        if (!Objects.equals(store.getOwnerId(), ownerId)) {
            throw new BadParameter("소유매장만 조회 가능");
        }
        return storeDto;
    }

    // 매장 정보 수정
    @Transactional
    public StoreDto storeUpdate(StoreUpdateDto storeUpdateDto) {
        storeCategoryDelete(storeUpdateDto.getStoreId());
        Store store = storeRepository.findById(storeUpdateDto.getStoreId())
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        store.updateStore(storeUpdateDto);
        List<String> tagNames = Arrays.stream(storeUpdateDto.getStoreTag().split(","))
                .map(String::trim).distinct().toList();
        categoryRegister(store, tagNames);
        storeRepository.flush();
        Store savedStore = storeRepository.findStoreWithCategories(storeUpdateDto.getStoreId()).orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));
        return StoreMapper.fromEntity(savedStore);
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
    public List<StoreDto> getStoreList(String keyword, StoreStatus status) {
        List<Store> stores = storeRepository.findByKeywordAndOptionalStatusWithGraph(keyword, status);
        return stores.stream().map(StoreMapper::fromEntity).collect(Collectors.toList());
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
    @Transactional(readOnly = true)
    public List<StoreResponseDto> getNearStoreList(Double latitude, Double longitude, String keyword) {
        // 1. 근처 매장 목록 조회
        List<Store> stores = storeRepository.findNearbyStoresOrderByDistanceAndKeyword(latitude, longitude, 500, keyword);
        List<Long> storeIds = stores.stream().map(Store::getStoreId).toList();

        if (storeIds.isEmpty()) {
            return List.of();
        }

        // 2. 한 번에 카테고리/핫딜 조회
        List<StoreCategory> storeCategories = storeCategoryRepository.findByStoreIds(storeIds);
        List<Hotdeal> hotdeals = hotdealRepository.findByStoreIds(storeIds);

        // 3. storeId 기준으로 그룹핑
        Map<Long, List<CategoryDto>> categoryMap = storeCategories.stream()
                .collect(Collectors.groupingBy(sc -> sc.getStore().getStoreId(),
                        Collectors.mapping(sc -> CategoryMapper.fromEntity(sc.getCategory()), Collectors.toList())));

        Map<Long, HotdealResponseDto> hotdealMap = hotdeals.stream()
                .collect(Collectors.toMap(h -> h.getStore().getStoreId(),
                        HotdealMapper::fromEntity,
                        (h1, h2) -> h1)); // 여러 개면 첫 번째만

        // 4. Store → StoreResponseDto 변환 + 거리계산 추가
        return stores.stream()
                .map(store -> {
                    double distance = DistanceCalculator.calculateDistance(latitude, longitude,
                            store.getLatitude(), store.getLongitude());
                    StoreResponseDto dto = StoreMapper.fromEntity(store, false, distance);
                    dto.setStoreTag(categoryMap.getOrDefault(store.getStoreId(), List.of()));
                    dto.setHotdeal(hotdealMap.get(store.getStoreId()));
                    return dto;
                })
                .toList();
    }

    // 근처 매장 조회(로그인시)
    @Transactional(readOnly = true)
    public List<StoreResponseDto> getNearStoreListforUser(Double latitude, Double longitude, Long userId, String keyword) {
        // 1. 근처 매장 목록 조회
        List<Store> stores = storeRepository.findNearbyStoresOrderByDistanceAndKeyword(latitude, longitude, 500, keyword);
        List<Long> storeIds = stores.stream().map(Store::getStoreId).toList();

        if (storeIds.isEmpty()) {
            return List.of();
        }

        // 2. 한 번에 카테고리/핫딜 조회
        List<StoreCategory> storeCategories = storeCategoryRepository.findByStoreIds(storeIds);
        List<Hotdeal> hotdeals = hotdealRepository.findByStoreIds(storeIds);

        // 3. 관심 매장 조회 (IN 절 한 번)
        List<Long> interestStoreIds = interestStoreRepository.findStoreIdsByUserIdAndStoreIds(userId, storeIds);

        // 4. Map 으로 매핑
        Map<Long, List<CategoryDto>> categoryMap = storeCategories.stream()
                .collect(Collectors.groupingBy(sc -> sc.getStore().getStoreId(),
                        Collectors.mapping(sc -> CategoryMapper.fromEntity(sc.getCategory()), Collectors.toList())));

        Map<Long, HotdealResponseDto> hotdealMap = hotdeals.stream()
                .collect(Collectors.toMap(h -> h.getStore().getStoreId(),
                        HotdealMapper::fromEntity,
                        (h1, h2) -> h1));

        Set<Long> interestSet = new HashSet<>(interestStoreIds);

        // 5. Store → StoreResponseDto 변환
        return stores.stream()
                .map(store -> {
                    double distance = DistanceCalculator.calculateDistance(
                            latitude, longitude, store.getLatitude(), store.getLongitude());

                    boolean isInterest = interestSet.contains(store.getStoreId());

                    StoreResponseDto dto = StoreMapper.fromEntity(store, isInterest, distance);
                    dto.setStoreTag(categoryMap.getOrDefault(store.getStoreId(), List.of()));
                    dto.setHotdeal(hotdealMap.get(store.getStoreId()));
                    return dto;
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
    @Transactional(readOnly = true)
    public List<StoreResponseDto> getMyInterestStores(Long userId) {
        // 1. 관심 매장 목록 조회
        List<InterestStore> interestList = interestStoreRepository.findAllByUserId(userId);
        List<Long> storeIds = interestList.stream()
                .map(InterestStore::getStoreId)
                .toList();

        if (storeIds.isEmpty()) {
            return List.of();
        }

        // 2. 매장 조회
        List<Store> stores = storeRepository.findAllByStoreIdIn(storeIds);

        // 3. 매장 핫딜 한 번에 조회
        List<Hotdeal> hotdeals = hotdealRepository.findByStoreIds(storeIds);

        // 4. storeId → HotdealResponseDto 매핑
        Map<Long, HotdealResponseDto> hotdealMap = hotdeals.stream()
                .collect(Collectors.toMap(h -> h.getStore().getStoreId(),
                        HotdealMapper::fromEntity,
                        (h1, h2) -> h1)); // 여러 개 있으면 첫 번째만

        // 5. 최종 DTO 조립
        return stores.stream()
                .map(store -> {
                    StoreResponseDto dto = StoreMapper.fromEntityForUser(store, true);
                    dto.setHotdeal(hotdealMap.get(store.getStoreId())); // hotdeal 세팅
                    return dto;
                })
                .toList();
    }

    // 매장 상세 조회
    @Transactional(readOnly = true)
    public StoreResponseDto getStoreById(Long storeId) {
        String userIdStr = GatewayRequestHeaderUtils.getUserId();
        long userId = -999L;
        if (userIdStr != null) {
            userId = Long.parseLong(userIdStr);
        }

        // 1. 매장 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFound("해당 매장을 찾을 수 없습니다."));

        // 2. 승인 상태 검증
        if (store.getStatus() == StoreStatus.PENDING) {
            throw new BadParameter("아직 승인을 기다리고 있는 매장입니다");
        } else if (store.getStatus() == StoreStatus.REJECTED) {
            throw new BadParameter("승인 거절된 매장입니다");
        }

        // 3. 관심 매장 여부 확인
        boolean isInterest = interestStoreRepository.existsByUserIdAndStoreId(userId, storeId);

        // 4. 카테고리 조회
        List<CategoryDto> categories = storeCategoryRepository.findByStoreId(storeId).stream()
                .map(sc -> CategoryMapper.fromEntity(sc.getCategory()))
                .toList();

        // 5. 핫딜 조회
        Optional<Hotdeal> hotdeal = hotdealRepository.findHotdealByStoreId(storeId);
        HotdealResponseDto hotdealDto = hotdeal.map(HotdealMapper::fromEntity).orElse(null);

        // 6. 최종 DTO 조립
        StoreResponseDto dto = StoreMapper.fromEntityForUser(store, isInterest);
        dto.setStoreTag(categories);
        dto.setHotdeal(hotdealDto);

        return dto;
    }
    // 근처 매장 조회(관심 많은 순, 랜딩페이지용)
    @Transactional(readOnly = true)
    public List<StoreResponseDto> getNearStoreListforUserByInterest(Double latitude, Double longitude) {
        String userIdStr = GatewayRequestHeaderUtils.getUserId();
        long userId;
        if(userIdStr!= null){
            userId = Long.parseLong(userIdStr);
        } else {
            userId = -999L;
        }
        return storeRepository.findNearbyStoresOrderByDistance(latitude, longitude, 500)
                .stream()
                .map(store -> {
                    double distance = DistanceCalculator.calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude());
                    boolean isInterest = interestStoreRepository.existsByUserIdAndStoreId(userId, store.getStoreId());
                    StoreResponseDto dto = StoreMapper.fromEntity(store, isInterest, distance);
                    dto.setInterestCount(interestStoreRepository.findAllByStoreId(store.getStoreId()).size());
                    return dto;
                })
                .sorted((a, b) -> Long.compare(b.getInterestCount(), a.getInterestCount()))
                .toList();
    }
}
