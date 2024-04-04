package org.example.javaasync.n6_completableFuture;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.javaasync.common.PlusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * 요구사항: 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
 *  + 스레드를 관리하고 싶음
 *  + 여러개의 비동기 작업의 결과물을 가지고 다시 비동기 호출
 *
 * 해결: CompletableFuture의 thenCombine() 사용
 *
 * 문제: 코드에 스레드풀 관리와 비즈니스 코드가 섞여있음
 */
@SpringBootApplication(scanBasePackages= "org.example.javaasync.common")
@Slf4j
public class CompletableFutureApplication3 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final var context = SpringApplication.run(CompletableFutureApplication3.class, args);
        final PlusService service = context.getBean(PlusService.class);
        final var startTime = System.currentTimeMillis();
        final var random = new Random();

        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        final var completableFuture1 = CompletableFuture.supplyAsync(
                () -> service.sendRequest(random.nextInt(100), random.nextInt(100))
                , executorService
        );
        final var completableFuture2 = CompletableFuture.supplyAsync(
                () -> service.sendRequest(random.nextInt(100), random.nextInt(100))
                , executorService
        );
        completableFuture1.thenCombine(
                    completableFuture2,
                    (t1, t2) -> service.sendRequest(t1, t2)
                )
              .thenAccept(t -> log.info(String.valueOf(t)));

        log.info("Main Finished, Elapsed: {}", System.currentTimeMillis() - startTime);
        Thread.sleep(3000);
        executorService.shutdown();
        context.close();

    }
}
