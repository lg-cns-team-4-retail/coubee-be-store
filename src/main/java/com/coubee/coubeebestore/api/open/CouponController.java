package com.coubee.coubeebestore.api.open;

import org.springframework.web.bind.annotation.*;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.common.web.context.GatewayRequestHeaderUtils;
import com.coubee.coubeebestore.domain.Coupon;
import com.coubee.coubeebestore.service.CouponService;

import java.util.List;

import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping(value = "/api/store/coupon", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    
    /* todo :
        1. 쿠폰 등록
        2. 개인별 쿠폰 목록 조회
        3. 쿠폰 사용
     */

     // 쿠폰 등록
     @RequestMapping("/{couponId}/register")
     public ApiResponseDto<String> couponRegister(@PathVariable Long couponId) {
        Long userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        couponService.registerCoupon(userId, couponId);
        return ApiResponseDto.defaultOk();
     }

     // 개인별 쿠폰 목록 조회
     @PostMapping("/myCoupons")
     public ApiResponseDto<List<Coupon>> getMyCouponList() {
        Long userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        List<Coupon> list = couponService.getMyCouponList(userId);
        return ApiResponseDto.readOk(list);
     }

}
