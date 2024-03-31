package org.example.javaasync.n7_springasync;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import org.example.javaasync.common.PlusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 요구사항: 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
 *  + 스레드를 관리하고 싶음
 *  + 여러개의 비동기 작업의 결과물을 가지고 다시 비동기 호출
 *  + 비동기 관리 코드와 비즈니스 코드를 분리
 *
 * 해결: Spring의 @Async, Executor Bean 사용
 */

@SpringBootApplication(scanBasePackages= "org.example.javaasync.common")
@Slf4j
public class SpringAsyncApplication {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final var context = SpringApplication.run(SpringAsyncApplication.class, args);
        final PlusAsyncService service = context.getBean(PlusAsyncService.class);

        final var startTime = System.currentTimeMillis();
        final var random = new Random();

        final var completableFuture1 = service.sendRequest(random.nextInt(100), random.nextInt(100));
        final var completableFuture2 = service.sendRequest(random.nextInt(100), random.nextInt(100));

        completableFuture1.thenCombine(completableFuture2,
                                       (t1, t2) -> service.sendRequest(t1, t2))
                          .thenCompose(c -> c) // 왜 필요한지 설명
                          .thenAccept(t -> log.info(String.valueOf(t)));
        log.info("Main Finished, Elapsed: {}", System.currentTimeMillis() - startTime);
        context.close();
    }

    @Configuration
    @EnableAsync
    public static class AsyncConfiguration {

        @Bean(destroyMethod = "shutdown")
        public Executor asyncExecutor() {
            final var executor = new ThreadPoolTaskExecutorBuilder()
                    .corePoolSize(10)
                    .maxPoolSize(10)
                    .threadNamePrefix("CustomTP-")
                    .build();
            executor.initialize();

            return executor;
        }
    }

    @Service
    @RequiredArgsConstructor
    public static class PlusAsyncService {
        private final PlusService plusService;
        @Async("asyncExecutor")
        public CompletableFuture<Integer> sendRequest(int param1, int param2) {
            return CompletableFuture.completedFuture(plusService.sendRequest(param1, param2));
        }
    }

}
