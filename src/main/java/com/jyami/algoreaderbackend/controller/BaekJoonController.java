package com.jyami.algoreaderbackend.controller;

import com.jyami.algoreaderbackend.dto.BaekJoonResDto;
import com.jyami.algoreaderbackend.dto.ResponseDto;
import com.jyami.algoreaderbackend.service.BaekJoonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BaekJoonController {

    private final BaekJoonService baekJoonService;

    @GetMapping("baekJoon/time/{userId}")
    public ResponseDto userProblemListWithTime(@PathVariable(name = "userId") String userId) {
        List<BaekJoonResDto> userProblemList = baekJoonService.getUserProblemListWithTime(userId);
        return ResponseDto.of(HttpStatus.OK, "시간에 따라 해당 유저가 푼 문제 객체 리턴 성공", userProblemList);
    }

    @GetMapping("baekJoon/{userId}")
    public ResponseDto userProblemList(@PathVariable(name = "userId") String userId) {
        List<BaekJoonResDto> userProblemList = baekJoonService.getUser(userId);
        return ResponseDto.of(HttpStatus.OK, "해당 유저가 푼 문제 객체 리턴 성공", userProblemList);
    }

}
