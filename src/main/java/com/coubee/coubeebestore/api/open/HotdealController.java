package com.coubee.coubeebestore.api.open;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.common.web.context.GatewayRequestHeaderUtils;
import com.coubee.coubeebestore.domain.dto.HotdealDto;
import com.coubee.coubeebestore.service.HotdealService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/store/admin/hotdeal", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class HotdealController {

    private final HotdealService hotdealService;

    // 매장 핫딜 켜기
    @PostMapping("/on")
    public ApiResponseDto<String> onHotdeal(@RequestBody HotdealDto hotdealDto) {
        Long adminId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        hotdealService.onHotdeal(adminId, hotdealDto);
        return ApiResponseDto.defaultOk();
    }

    // 매장 핫딜 끄기
    @PostMapping("/off/{storeId}")
    public ApiResponseDto<String> offHotdeal(@PathVariable Long storeId) {
        Long adminId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        hotdealService.offHotdeal(adminId, storeId);
        return ApiResponseDto.defaultOk();
    }
}
