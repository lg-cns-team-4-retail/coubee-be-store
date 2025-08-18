package com.coubee.coubeebestore.api.backend;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.service.BackendStoreService;
import com.coubee.coubeebestore.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/backend/store", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BackendStoreController {
    private final BackendStoreService backendStoreService;

    @GetMapping(value = "/near")
    public ApiResponseDto<List<Long>> getNearStoreIds(@RequestParam double latitude, @RequestParam double longitude) {
        return ApiResponseDto.createOk(backendStoreService.getNearStoreIds(latitude, longitude));
    }

}
