package com.coubee.coubeebestore.api.open;

import org.springframework.web.bind.annotation.*;

import com.coubee.coubeebestore.common.dto.ApiResponseDto;
import com.coubee.coubeebestore.domain.Coupon;
import com.coubee.coubeebestore.domain.dto.CouponCreateDto;
import com.coubee.coubeebestore.domain.dto.CouponResponseDto;
import com.coubee.coubeebestore.service.CouponService;

import java.util.List;

import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping(value = "/api/store/admin/coupon", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CouponAdminController {

    private final CouponService couponService;

    /* todo :
        1. 쿠폰 생성 v
        2. 매장별 쿠폰 목록 조회 v
        3. 쿠폰 내용 상세 조회(?)
        4. 쿠폰 삭제 v
     */

     // 쿠폰 생성
     @RequestMapping("/create")
     public ApiResponseDto<String> couponCreate(@RequestBody CouponCreateDto couponCreateDto) {
        couponService.createCoupon(couponCreateDto);
        return ApiResponseDto.defaultOk();
     }

     // 매장별 쿠폰 목록 조회
    // coupon 조회시 enddate가 지났을때 Expired로 상태 변경후 리턴
     @GetMapping("/list")
     public ApiResponseDto<List<Coupon>> couponList(@RequestParam Long storeId) {
        List<Coupon> list = couponService.getCouponList(storeId);
        return ApiResponseDto.readOk(list);
     }

     // 쿠폰 삭제
     @DeleteMapping("/delete/{couponId}")
     public ApiResponseDto<String> couponDelete(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ApiResponseDto.defaultOk();
     }

     // 쿠폰 상세 조회
     @GetMapping("/list/{couponId}")
     public ApiResponseDto<CouponResponseDto> couponView(@PathVariable Long couponId, @RequestBody CouponResponseDto couponResponseDto) {
         CouponResponseDto coupon = couponService.viewCoupon(couponId, couponResponseDto);
         return ApiResponseDto.readOk(coupon);
     }

}
