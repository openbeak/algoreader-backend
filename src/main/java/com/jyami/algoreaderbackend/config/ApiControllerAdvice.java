package com.jyami.algoreaderbackend.config;


import com.jyami.algoreaderbackend.dto.ResponseDto;
import com.jyami.algoreaderbackend.exception.UnTrackedIOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    @ExceptionHandler(UnTrackedIOException.class)
    public ResponseDto<ResponseDto> handleCustomAuthException(UnTrackedIOException ex) {
        return ResponseDto.FAIL_DEFAULT_RES;
    }

}