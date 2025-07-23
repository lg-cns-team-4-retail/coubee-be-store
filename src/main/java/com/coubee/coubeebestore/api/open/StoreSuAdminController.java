package com.coubee.coubeebestore.api.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/store/su", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreSuAdminController {

    /* todo :
        1. 매장 등록 승인
        2. 매장 삭제
     */
}
