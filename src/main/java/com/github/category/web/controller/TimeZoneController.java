package com.github.category.web.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TimeZoneController {

    @PostMapping("/set-timezone")
    public String setTimeZone(@RequestBody Map<String, String> request) {
        // 클라이언트에서 보내온 시간대 정보
        String userTimeZone = request.get("timeZone");

        // 유저의 시간대에 맞는 ZonedDateTime 생성
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of(userTimeZone));

        // 시간대에 맞는 시간을 확인하여 출력
        return "User's time is set to: " + dateTime.toString();
    }
}

//프론트에서 유저 시간 정보 request로 받아오기
//// 브라우저에서 사용자의 시간대를 자동으로 추출
//const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
//
//// 서버에 시간대 정보를 포함해서 POST 요청을 보냄
//fetch('http://localhost:8080/api/set-timezone', {
//    method: 'POST',
//    headers: {
//        'Content-Type': 'application/json',
//    },
//    body: JSON.stringify({
//        timeZone: userTimeZone
//    }),
//})
//.then(response => response.text())
//.then(data => {
//    console.log(data);  // 서버로부터의 응답 출력
//})
//.catch(error => {
//    console.error('Error:', error);
//});