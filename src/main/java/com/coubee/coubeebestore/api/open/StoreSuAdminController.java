package com.coubee.coubeebestore.api.open;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.StoreStatus;
import com.coubee.coubeebestore.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(value = "/api/store/su", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreSuAdminController {

    private final StoreService storeService;

    /* todo :
        1. 매장 등록 승인v
        2. 매장 삭제
        3. 매장 등록 대기 목록 조회(?)v
     */

    // 매장 등록 승인
    @PostMapping("/approve/{storeId}")
    public ApiResponseDto<String> storeApprove(@PathVariable Long storeId) {
        storeService.storeApprove(storeId);
        return ApiResponseDto.defaultOk();
    }

    // 매장 등록 반려
    @PostMapping("/reject/{storeId}")
    public ApiResponseDto<String> storeReject(@PathVariable Long storeId, @RequestBody String rejectReason) {
        storeService.storeReject(storeId, rejectReason);
        return ApiResponseDto.defaultOk();
    }

    // 매장 등록 대기 목록 조회
    @GetMapping("/status")
    public ApiResponseDto<List<Store>> getStatusList() {
        List<Store> list = storeService.getStatusList(StoreStatus.PENDING);
        return ApiResponseDto.readOk(list);
    }

    // 매장 삭제 승인
    @PostMapping("/delete/{storeId}")
    public ApiResponseDto<String> storeDelete(@PathVariable Long storeId) {
        storeService.storeDelete(storeId);
        return ApiResponseDto.defaultOk();
    }
    
}
