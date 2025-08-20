package com.coubee.coubeebestore.api.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.service.BackendStoreService;

import java.util.List;

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
    public ApiResponseDto<List<Long>> getNearStoreIds(@RequestParam double latitude, @RequestParam double longitude) {
        return ApiResponseDto.createOk(backendStoreService.getNearStoreIds(latitude, longitude));
    }
}
