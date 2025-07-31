package com.coubee.coubeebestore.api.open;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.common.web.context.GatewayRequestHeaderUtils;
import com.coubee.coubeebestore.domain.Store;
import com.coubee.coubeebestore.domain.dto.StoreDto;
import com.coubee.coubeebestore.domain.dto.StoreRegisterDto;
import com.coubee.coubeebestore.domain.dto.StoreResponseDto;
import com.coubee.coubeebestore.domain.dto.StoreUpdateDto;
import com.coubee.coubeebestore.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/api/store/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreAdminController {

    private final StoreService storeService;

     /* todo :
        1. 매장 생성 요청 v
        2. 매장 목록 조회 v
        3. 매장 백그라운드 이미지 저장, 매장 프로필 사진 저장 v
        4. 매장 정보 수정 v
     */

    // 매장 생성 요청
    @PostMapping("/register")
    public ApiResponseDto<String> storeRegister(@RequestBody StoreRegisterDto storeRegisterDto) {
        Long adminId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        storeService.storeRegister(adminId, storeRegisterDto);
        return ApiResponseDto.defaultOk();
    }

    // 매장 목록 조회
    @GetMapping("/list")
    public ApiResponseDto<List<Store>> getStoreList() {
        Long adminId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        List<Store> list = storeService.getStoreList(adminId);
        return ApiResponseDto.readOk(list);
    }

    // 매장 백그라운드 이미지
    @PostMapping("/img/background")
    public ApiResponseDto<String> storeImgBackground(@RequestParam MultipartFile file) {
        String imgUrl = storeService.storeImgBackground(file);
        return ApiResponseDto.createOk(imgUrl);
    }

    // 매장 프로필 이미지
    @PostMapping("/img/profile")
    public ApiResponseDto<String> storeImgProfile(@RequestParam MultipartFile file) {
        String imgUrl = storeService.storeImgProfile(file);
        return ApiResponseDto.createOk(imgUrl);
    }

    // 사업자등록증
    @PostMapping("/img/certificate")
    public ApiResponseDto<String> storeImgCertificate(@RequestParam MultipartFile file) {
        String imgUrl = storeService.storeImgCertificate(file);
        return ApiResponseDto.createOk(imgUrl);
    }

    // 매장 정보 수정
    @PostMapping("/update")
    public ApiResponseDto<String> storeUpdate(@RequestBody StoreUpdateDto storeUpdateDto) {
        storeService.storeUpdate(storeUpdateDto);
        return ApiResponseDto.defaultOk();
    }

    // 매장 상세 조회(점주)
    @GetMapping("list/{storeId}")
    public ApiResponseDto<?> getStoreById(@PathVariable Long storeId) {
        Long adminId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        StoreDto store = storeService.getStoreById(adminId, storeId);
        return ApiResponseDto.readOk(store);
    }
}
