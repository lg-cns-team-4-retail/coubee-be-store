package com.coubee.coubeebestore.api.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.domain.dto.StoreResponseDto;
import com.coubee.coubeebestore.service.BackendStoreService;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/backend/store", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BackendStoreController {
    private final BackendStoreService backendStoreService;

    @GetMapping(value = "/hello")
    public ApiResponseDto<String> hello() {
        return ApiResponseDto.createOk("feignClient 성공");
    }

    @GetMapping(value = "/near")
    public ApiResponseDto<List<Long>> getNearStoreIds(@RequestParam double latitude, @RequestParam double longitude, @RequestParam(required = false, defaultValue = "") String keyword) {
        return ApiResponseDto.createOk(backendStoreService.getNearStoreIds(latitude, longitude, keyword));
    }

    @GetMapping(value = "/validate")
    public ApiResponseDto<Boolean> validStoreId(@RequestParam Long storeId) {
        return ApiResponseDto.readOk(backendStoreService.validStoreId(storeId));
    }

    @GetMapping(value = "/bulk")
    public ApiResponseDto<Map<Long, StoreResponseDto>> getStoresByIds(@RequestParam List<Long> storeIds) {
        return ApiResponseDto.readOk(backendStoreService.getStoresByIds(storeIds));
    }
    
    @GetMapping(value = "/owner/{ownerId}/approved-stores")
    public ApiResponseDto<List<Long>> getStoresByOwnerIdOnApproved(@RequestParam Long ownerId) {
        return ApiResponseDto.readOk(backendStoreService.getStoresByOwnerIdOnApproved(ownerId));
    }

    @GetMapping(value = "/{storeId}/validate-status")
    public ApiResponseDto<Boolean> validStatusByStoreId(@RequestParam Long storeId) {
        return ApiResponseDto.readOk(backendStoreService.validStatusByStoreId(storeId));
    }
}
