package com.coubee.coubeebestore.api.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.domain.repository.StoreRepository;

import antlr.collections.List;

import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/backend/store/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BackendStoreController {
    private final StoreRepository storeRepository;

    @GetMapping(value = "/hello")
    public ApiResponseDto<String> hello() {
        return ApiResponseDto.createOk("feingClient 성공");
    }

    // @GetMapping("/storeList")
    // public ApiResponseDto<List> storeList(Long userId) {
    //     List list = storeRepository.findNearbyStoresOrderByDistance(userId);
    //     return ApiResponseDto.readOk(list);
    // }
}
