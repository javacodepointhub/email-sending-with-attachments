package com.javacodepoint.email.config;

import com.javacodepoint.email.exception.EmailAsyncExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * Thread pool used for email processing.
     */
    @Bean(name = "emailExecutor")
    public Executor emailExecutor() {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);

        executor.setMaxPoolSize(10);

        executor.setQueueCapacity(100);

        executor.setThreadNamePrefix(
                "EMAIL-THREAD-"
        );

        executor.initialize();

        return executor;
    }

    /**
     * Handles uncaught exceptions thrown
     * from @Async methods.
     */
    @Override
    public AsyncUncaughtExceptionHandler
    getAsyncUncaughtExceptionHandler() {

        return new EmailAsyncExceptionHandler();
    }
}