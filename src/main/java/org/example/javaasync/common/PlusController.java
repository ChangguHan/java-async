package org.example.javaasync.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlusController {
    @GetMapping("/plus")
    public int plus(@RequestParam int param1, @RequestParam int param2) throws InterruptedException {
        Thread.sleep(100);
        return param1 + param2;
    }
}
