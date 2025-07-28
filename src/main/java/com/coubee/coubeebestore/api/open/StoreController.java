package com.coubee.coubeebestore.api.open;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.common.web.context.GatewayRequestHeaderUtils;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.dto.StoreDto;
import com.coubee.coubeebestore.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/store", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /* todo :
        1. 매장 목록 조회 (위치기반)
        2. 매장 목록 조회 (personalizer 매장 추천 storeIds [] )
        3. 관심 매장 등록
        4. 매장 검색 기능
     */

    @GetMapping("/near")
    public ApiResponseDto<?> getNearStoreList(@RequestParam double lat, @RequestParam double lng) {
        List<Store> storeList = storeService.getNearStoreList(lat, lng);
        return ApiResponseDto.readOk(storeList);
    }

    @PostMapping("/{storeId}/interest")
    public ApiResponseDto<String> addInterest(@RequestBody StoreDto storeDto) {
        Long userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        storeService.addInterestStore(userId, storeDto);
        return ApiResponseDto.defaultOk();
    }
}
