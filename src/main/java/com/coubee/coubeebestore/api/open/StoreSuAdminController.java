package com.coubee.coubeebestore.api.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.common.web.context.GatewayRequestHeaderUtils;
import com.coubee.coubeebestore.domain.dto.StoreDto;
import com.coubee.coubeebestore.domain.dto.StoreRegisterDto;
import com.coubee.coubeebestore.service.StoreService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
    @PostMapping("/approve")
    public ApiResponseDto<String> storeApprove(@RequestBody StoreDto storeDto) {
        storeService.storeApprove(storeDto);
        return ApiResponseDto.defaultOk();
    } 

    // 매장 등록 대기 목록 조회
    @PostMapping("/status")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
}
