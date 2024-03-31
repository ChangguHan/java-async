package org.example.javaasync.n1_thread;

import org.example.javaasync.common.PlusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * 요구사항: 기다려야하는 작업(API 호출)을 동시에 어려번 호출하고 싶을때
 *Ï
 * 해결: Thread를 사용해서 응답을 기다리지 않고 여러번 호출
 *
 * 문제: 작업할때마다 스레드 생성, 이 스레드를 관리하고 싶음!
 */
@SpringBootApplication(scanBasePackages= "org.example.javaasync.common")
@Slf4j
public class ThreadApplication {

    public static void main(String[] args) throws InterruptedException {
        final var context = SpringApplication.run(ThreadApplication.class, args);
        final PlusService service = context.getBean(PlusService.class);
        final var startTime = System.currentTimeMillis();

        for (int i=0; i<10; i++) {
            new Thread(() -> service.sendRequest(1, 2)).start();
        }

        log.info("Main Finished, Elapsed: {}", System.currentTimeMillis() - startTime);
        Thread.sleep(3000);
        context.close();
    }

}
