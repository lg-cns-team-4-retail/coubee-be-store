package com.coubee.coubeebestore.api.open;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.domain.StoreStatus;
import com.coubee.coubeebestore.domain.dto.StoreDto;
import com.coubee.coubeebestore.domain.dto.StoreRejectDto;
import com.coubee.coubeebestore.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/api/store/su", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreSuAdminController {

    private final StoreService storeService;

    // 매장 등록 승인
    @PostMapping("/approve/{storeId}")
    public ApiResponseDto<String> storeApprove(@PathVariable Long storeId) {
        storeService.storeApprove(storeId);
        return ApiResponseDto.defaultOk();
    }

    // 매장 등록 반려
    @PostMapping("/reject/{storeId}")
    public ApiResponseDto<String> storeReject(@PathVariable Long storeId, @RequestBody StoreRejectDto dto) {
        storeService.storeReject(storeId, dto);
        return ApiResponseDto.defaultOk();
    }

    // 매장 등록 대기 목록 조회
    @GetMapping("/status")
    public ApiResponseDto<List<StoreDto>> getStatusList() {
        List<StoreDto> list = storeService.getStatusList(StoreStatus.PENDING);
        return ApiResponseDto.readOk(list);
    }
    // 매장 전체 조회(페이징처리)
    @GetMapping("/list")
    public ApiResponseDto<List<StoreDto>> getStoreList(@RequestParam String keyword, @RequestParam String status) {
        List<StoreDto> list = storeService.getStoreList(keyword,status);
        return ApiResponseDto.readOk(list);
    }
    // 매장 삭제
    @DeleteMapping("/delete/{storeId}")
    public void storeDelete(@PathVariable Long storeId) {
        storeService.storeDelete(storeId);
    }
}
