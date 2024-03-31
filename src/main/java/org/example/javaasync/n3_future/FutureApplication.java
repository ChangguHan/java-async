package org.example.javaasync.n3_future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.example.javaasync.common.PlusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * 요구사항: 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
 *  + 스레드를 관리하고 싶음
 *  + 비동기 작업의 결과값을 받아오고 싶음
 *
 * 해결: Future 사용해서, get()으로 가져옴
 *
 * 문제 : get()을 하는순간 기다리기 때문에 비동기의 의마가 사라짐
 *
 */
@SpringBootApplication(scanBasePackages= "org.example.javaasync.common")
@Slf4j
public class FutureApplication {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final var context = SpringApplication.run(FutureApplication.class, args);
        final PlusService service = context.getBean(PlusService.class);
        final var startTime = System.currentTimeMillis();

        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i=0; i<10; i++) {
            final Future<Integer> future = executorService.submit(() -> service.sendRequest(1, 2));
            log.info(String.valueOf(future.get()));
        }

        log.info("Main Finished, Elapsed: {}", System.currentTimeMillis() - startTime);
        Thread.sleep(3000);
        executorService.shutdown();
        context.close();
    }

}
