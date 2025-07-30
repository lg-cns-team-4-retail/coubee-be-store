package com.coubee.coubeebestore.api.open;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.common.web.context.GatewayRequestHeaderUtils;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping(value = "/api/store", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /* todo :
        1. 매장 목록 조회 (위치기반) v
        2. 매장 목록 조회 (personalizer 매장 추천 storeIds [] )
        3. 관심 매장 등록 v
        4. 매장 검색 기능 v
        5. 매장 상세 조회 v
     */

    // 매장 목록 조회(위치기반)
    @GetMapping("/near")
    public ApiResponseDto<?> getNearStoreList(@RequestParam double lat, @RequestParam double lng) {
        List<Store> storeList = storeService.getNearStoreList(lat, lng);
        return ApiResponseDto.readOk(storeList);
    }

    // 관심 매장 등록
    @PostMapping("/interest/{storeId}")
    public ApiResponseDto<String> addInterest(@PathVariable Long storeId) {
        Long userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        storeService.addInterestStore(userId, storeId);
        return ApiResponseDto.defaultOk();
    }

    // 나의 관심 매장 조회
    @GetMapping("/interest/my")
    public ApiResponseDto<?> getMyInterestStores(){
        Long userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        List<Store> stores = storeService.getMyInterestStores(userId);
        return ApiResponseDto.readOk(stores);
    }

    // 매장 검색(매장 이름, 키워드 조회)
    @GetMapping("/search")
    public ApiResponseDto<?> searchStores(@RequestParam String keyword) {
        List<Store> stores = storeService.getSearchStores(keyword);
        return ApiResponseDto.readOk(stores);
    }

    // 매장 상세 조회
    @GetMapping("/detail/{storeId}")
    public ApiResponseDto<?> getDetailStore(@PathVariable Long storeId) {
        Store store = storeService.getDetailStore(storeId);
        return ApiResponseDto.readOk(store);
    }
    
}
