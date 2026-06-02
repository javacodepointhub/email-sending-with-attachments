package com.javacodepoint.email.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class EmailAsyncExceptionHandler
        implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(

            Throwable throwable,

            Method method,

            Object... params) {

        log.error(
                "Async Method Failure. Method={}, Params={}",
                method.getName(),
                Arrays.toString(params),
                throwable
        );
    }
}