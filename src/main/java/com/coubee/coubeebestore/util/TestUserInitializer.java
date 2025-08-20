package com.coubee.coubeebestore.util;

import com.coubee.coubeebestore.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Component
//@RequiredArgsConstructor
//@Slf4j
public class TestUserInitializer {
//    private final StoreRepository storeRepository;
//
//    @Transactional
//    @org.springframework.context.event.EventListener(
//            org.springframework.boot.context.event.ApplicationReadyEvent.class
//    )
//    public void onReady() {
//        log.info("TestUserInitializer start (ApplicationReadyEvent)");
//        storeRepository.findAll().forEach(u -> {
//            String backImg = u.getBackImg();
//            String profileImg = u.getProfileImg();
//            String bizImg = u.getBizImg();
//            if (bizImg != null) {
//                if (bizImg.startsWith("/store")) {
//                    bizImg = "https://d1du1htkbm5yt2.cloudfront.net" + bizImg;
//                    u.setBizImg(bizImg);
//                }
//            }
//            if (profileImg != null) {
//                if (profileImg.startsWith("/store")) {
//                    profileImg = "https://d1du1htkbm5yt2.cloudfront.net" + profileImg;
//                    u.setProfileImg(profileImg);
//                }
//            }
//            if (backImg != null) {
//                if (backImg.startsWith("/store")) {
//                    backImg = "https://d1du1htkbm5yt2.cloudfront.net" + backImg;
//                    u.setBackImg(backImg);
//                }
//            }
//        });
//        log.info("TestUserInitializer done");
//    }
}
