package com.coubee.coubeebestore.api.open;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        3. 매장 등록 대기 목록 조회(?)
     */

    // 매장 등록 승인
    @PostMapping("/approve/{storeId}")
    public ApiResponseDto<String> storeApprove(@PathVariable Long storeId) {
        storeService.storeApprove(storeId);
        return ApiResponseDto.defaultOk();
    }
    @PostMapping("/reject/{storeId}")
    public ApiResponseDto<String> storeReject(@PathVariable Long storeId,@RequestBody String rejectReason) {
        storeService.storeReject(storeId,rejectReason);
        return ApiResponseDto.defaultOk();
    }

    // 매장 등록 대기 목록 조회
    @PostMapping("/status")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
}
