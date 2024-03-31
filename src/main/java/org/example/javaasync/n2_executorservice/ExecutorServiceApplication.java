package org.example.javaasync.n2_executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.javaasync.common.PlusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * 요구사항: 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
 * + 스레드를 관리하고 싶음
 *
 * 해결: ExecutorService 사용
 *
 * 추가 요구사항: 비동기 작업의 결과값을 받아오고 싶음
 */
@SpringBootApplication(scanBasePackages= "org.example.javaasync.common")
@Slf4j
public class ExecutorServiceApplication {

    public static void main(String[] args) throws InterruptedException {
        final var context = SpringApplication.run(ExecutorServiceApplication.class, args);
        final PlusService service = context.getBean(PlusService.class);
        final var startTime = System.currentTimeMillis();

        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i=0; i<10; i++) {
            executorService.submit(() -> service.sendRequest(1, 2));
        }

        log.info("Main Finished, Elapsed: {}", System.currentTimeMillis() - startTime);
        Thread.sleep(3000);
        executorService.shutdown();
        context.close();
    }
}


//        final ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i=0; i<10; i++) {
//            executorService.submit(() -> service.sendRequest(1, 2));
//            Thread.sleep(300);
//        }


//        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
//        for (int i=0; i<10; i++) {
//            executorService.schedule(() -> service.sendRequest(1, 2), 1000, TimeUnit.MILLISECONDS);
//        }


//해볼것: ExecutorService, Fixed(숫자 바꾸기 10->5), cached, scheduled