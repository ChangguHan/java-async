package org.example.javaasync.common;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlusService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String URL_FORMAT = "http://127.0.0.1:18083/plus?param1=%d&param2=%d";

    public int sendRequest(int param1, int param2) {
        final var startTime = System.currentTimeMillis();

        final var result = restTemplate.getForObject(String.format(URL_FORMAT, param1, param2), int.class);
        log.info("{} + {} = {}, Elapsed: {}", param1, param2, result, System.currentTimeMillis() - startTime);
        return result;
    }
}
