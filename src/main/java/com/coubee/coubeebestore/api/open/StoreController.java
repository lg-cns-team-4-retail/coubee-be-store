package com.coubee.coubeebestore.api.open;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.common.web.context.GatewayRequestHeaderUtils;
import com.coubee.coubeebestore.domain.dto.StoreResponseDto;
import com.coubee.coubeebestore.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/api/store", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 매장 목록 조회(위치기반)
//    @GetMapping("/near")
//    public ApiResponseDto<List<StoreResponseDto>> getNearStoreList(@RequestParam double lat, @RequestParam double lng, @RequestParam(required = false, defaultValue = "") String keyword) {
//        if(GatewayRequestHeaderUtils.getUserId() != null) {
//            Long userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
//            List<StoreResponseDto> storeList = storeService.getNearStoreListforUser(lat, lng, userId, keyword);
//            return ApiResponseDto.readOk(storeList);
//        } else {
//            List<StoreResponseDto> storeList = storeService.getNearStoreList(lat, lng, keyword);
//            return ApiResponseDto.readOk(storeList);
//        }
//    }
    @GetMapping("/near")
    public ApiResponseDto<Page<StoreResponseDto>> getNearStoreList(@RequestParam double lat, @RequestParam double lng, @RequestParam(required = false, defaultValue = "") String keyword, @PageableDefault(page=0,size = 10) Pageable pageable) {
        if(GatewayRequestHeaderUtils.getUserId() != null) {
            Long userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
            Page<StoreResponseDto> storeList = storeService.getNearStoreListforUser(lat, lng, userId, keyword,pageable);
            return ApiResponseDto.readOk(storeList);
        } else {
            Page<StoreResponseDto> storeList = storeService.getNearStoreList(lat, lng, keyword,pageable);
            return ApiResponseDto.readOk(storeList);
        }
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
    public ApiResponseDto<List<StoreResponseDto>> getMyInterestStores(){
        Long userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        List<StoreResponseDto> stores = storeService.getMyInterestStores(userId);
        return ApiResponseDto.readOk(stores);
    }

    // 매장 상세 조회
    @GetMapping("/detail/{storeId}")
    public ApiResponseDto<?> getStoreById(@PathVariable Long storeId) {
        StoreResponseDto store = storeService.getStoreById(storeId);
        return ApiResponseDto.readOk(store);
    }

    // 추천 매장 목록(위치기반)
    @GetMapping("/near/interest")
    public ApiResponseDto<List<StoreResponseDto>> getNearStoreListByInterest(@RequestParam double lat, @RequestParam double lng) {
            List<StoreResponseDto> storeList = storeService.getNearStoreListforUserByInterest(lat, lng);
            return ApiResponseDto.readOk(storeList);
    }
    
}
