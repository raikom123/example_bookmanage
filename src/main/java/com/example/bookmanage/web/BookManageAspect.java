package com.example.bookmanage.web;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class BookManageAspect {

    @Around("execution(* com.example.bookmanage.web.*.*(..))")
    public Object inWebLayer(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result;
        try {
            result = pjp.proceed();
        } finally {
            stopWatch.stop();
            log.trace("{} : {} ms", pjp.getSignature(), stopWatch.getTotalTimeMillis());
        }
        return result;
    }

    @Around("execution(* com.example.bookmanage.service.*.*(..))")
    public Object inServiceLayer(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result;
        try {
            result = pjp.proceed();
        } finally {
            stopWatch.stop();
            log.trace("{} : {} ms", pjp.getSignature(), stopWatch.getTotalTimeMillis());
        }
        return result;
    }

}
