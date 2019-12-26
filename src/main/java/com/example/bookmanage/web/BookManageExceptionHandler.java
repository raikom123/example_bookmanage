package com.example.bookmanage.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hri
 */
@Slf4j
@ControllerAdvice
public class BookManageExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public String handleException(HttpServletRequest request, Exception e) {
        log.error("system error!", e);
        return "error";
    }

}
