//package org.example.javaasync2.common;
//
//import java.util.concurrent.CompletableFuture;
//
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PlusAsyncService {
//    private final PlusService plusService;
//    private final RestTemplate restTemplate = new RestTemplate();
//    private static final String URL_FORMAT = "http://127.0.0.1:18083/plus?param1=%d&param2=%d";
//
//    @Async
//    public CompletableFuture<Integer> sendRequest(int param1, int param2) {
//        return CompletableFuture.completedFuture(plusService.sendRequest(param1, param2));
//    }
//}
